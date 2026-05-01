document.addEventListener('DOMContentLoaded', () => {
    const resourcesListDiv = document.getElementById('resourcesList');
    const bookingForm = document.getElementById('bookingForm');
    const resourceSelect = document.getElementById('resourceSelect');
    const startTimeInput = document.getElementById('startTime');
    const endTimeInput = document.getElementById('endTime');
    const bookingAlert = document.getElementById('bookingAlert');

    const bookingsListDiv = document.getElementById('bookingsList');
    const filterStartTimeInput = document.getElementById('filterStartTime');
    const filterEndTimeInput = document.getElementById('filterEndTime');
    const filterBookingsBtn = document.getElementById('filterBookingsBtn');

    // Assume the backend API is running on the same host and port
    const API_BASE_URL = window.location.origin + '/api';

    // --- Helper Functions ---

    function showAlert(message, type = 'success') {
        bookingAlert.className = `alert alert-${type} alert-dismissible fade show`;
        bookingAlert.innerHTML = `${message} <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>`;
        bookingAlert.classList.remove('d-none');
    }

    function formatDateTimeForInput(isoString) {
        // Formats ISO 8601 string to YYYY-MM-DDTHH:MM for datetime-local input
        if (!isoString) return '';
        return isoString.slice(0, 16);
    }

    function formatStatus(status) {
        switch(status.toLowerCase()) {
            case 'active': return '<span class="status-active">فعال</span>';
            case 'inactive': return '<span class="status-inactive">غیرفعال</span>';
            case 'reserved': return '<span class="status-reserved">رزرو شده</span>';
            case 'cancelled': return '<span class="status-cancelled">لغو شده</span>';
            default: return status;
        }
    }

    // --- Fetch Resources ---
    async function fetchResources() {
        try {
            const response = await fetch(`${API_BASE_URL}/resources`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const resources = await response.json();

            resourcesListDiv.innerHTML = ''; // Clear loading message
            resourceSelect.innerHTML = '<option value="">-- انتخاب منبع --</option>'; // Clear previous options

            if (resources.length === 0) {
                resourcesListDiv.innerHTML = '<p>هیچ منبعی یافت نشد.</p>';
                return;
            }

            resources.forEach(resource => {
                // Display in resources list section
                const resourceDiv = document.createElement('div');
                resourceDiv.className = 'resource-item';
                resourceDiv.innerHTML = `
                    <h5>${resource.name}</h5>
                    <p>وضعیت: ${formatStatus(resource.status)}</p>
                    <button class="btn btn-sm btn-outline-primary" onclick="populateBookingForm(${JSON.stringify(resource)})">رزرو این منبع</button>
                `;
                resourcesListDiv.appendChild(resourceDiv);

                // Add to booking form select dropdown
                const option = document.createElement('option');
                option.value = resource.id;
                option.textContent = resource.name;
                // Optionally set resource status or other info if needed
                resourceSelect.appendChild(option);
            });

        } catch (error) {
            console.error('Error fetching resources:', error);
            resourcesListDiv.innerHTML = '<p class="text-danger">خطا در بارگذاری منابع. لطفاً صفحه را دوباره بارگذاری کنید.</p>';
        }
    }

    // Helper to populate form when clicking "رزرو این منبع"
    window.populateBookingForm = function(resource) {
        resourceSelect.value = resource.id;
        // Optionally pre-fill dates if you have a logic for default booking times
        // For now, just select the resource. User fills times.
    }

    // --- Create Booking ---
    bookingForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        bookingAlert.classList.add('d-none'); // Hide previous alert

        const resourceId = resourceSelect.value;
        const startTime = startTimeInput.value;
        const endTime = endTimeInput.value;

        if (!resourceId || !startTime || !endTime) {
            showAlert('لطفاً تمام فیلدهای رزرو را تکمیل کنید.', 'warning');
            return;
        }

        const bookingData = {
            resourceId: resourceId,
            startTime: new Date(startTime).toISOString(), // Send as ISO string
            endTime: new Date(endTime).toISOString()     // Send as ISO string
        };

        try {
            const response = await fetch(`${API_BASE_URL}/bookings`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(bookingData)
            });

            const result = await response.json();

            if (!response.ok) {
                // Try to get specific error message from backend, or use generic
                const errorMessage = result.message || `خطا در ایجاد رزرو: ${response.statusText}`;
                showAlert(errorMessage, 'danger');
                return;
            }

            showAlert('رزرو با موفقیت ایجاد شد!', 'success');
            bookingForm.reset(); // Clear the form
            resourceSelect.value = ""; // Reset select to default
            await fetchBookings(); // Refresh the bookings list
            await fetchResources(); // Refresh resources to update status/availability if needed

        } catch (error) {
            console.error('Error creating booking:', error);
            showAlert('خطا در ارتباط با سرور. لطفاً دوباره تلاش کنید.', 'danger');
        }
    });

    // --- Fetch and Display Bookings ---
    async function fetchBookings() {
        bookingsListDiv.innerHTML = '<p>در حال بارگذاری رزروها...</p>'; // Loading indicator

        let url = `${API_BASE_URL}/bookings`;
        const filterStart = filterStartTimeInput.value;
        const filterEnd = filterEndTimeInput.value;

        const params = new URLSearchParams();
        if (filterStart) params.append('startTime', new Date(filterStart).toISOString());
        if (filterEnd) params.append('endTime', new Date(filterEnd).toISOString());

        if (params.toString()) {
            url += `?${params.toString()}`;
        }

        try {
            const response = await fetch(url);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const bookings = await response.json();

            bookingsListDiv.innerHTML = ''; // Clear loading message

            if (bookings.length === 0) {
                bookingsListDiv.innerHTML = '<p>هیچ رزروی در بازه زمانی مشخص شده یافت نشد.</p>';
                return;
            }

            bookings.forEach(booking => {
                const bookingDiv = document.createElement('div');
                bookingDiv.className = 'booking-item';
                bookingDiv.innerHTML = `
                    <h5>رزرو برای: ${booking.resourceName || 'منبع ناشناس'}</h5>
                    <p>
                        زمان شروع: ${booking.startTime ? formatDateTimeForInput(booking.startTime) : 'نامشخص'} <br>
                        زمان پایان: ${booking.endTime ? formatDateTimeForInput(booking.endTime) : 'نامشخص'} <br>
                        وضعیت: ${formatStatus(booking.status)}
                    </p>
                    ${booking.status === 'RESERVED' ? `
                        <button class="btn btn-sm btn-danger" onclick="cancelBooking(${booking.id})">لغو رزرو</button>
                    ` : ''}
                `;
                bookingsListDiv.appendChild(bookingDiv);
            });

        } catch (error) {
            console.error('Error fetching bookings:', error);
            bookingsListDiv.innerHTML = '<p class="text-danger">خطا در بارگذاری رزروها. لطفاً صفحه را دوباره بارگذاری کنید.</p>';
        }
    }

    // --- Cancel Booking ---
    window.cancelBooking = async function(bookingId) {
        if (!confirm('آیا از لغو این رزرو مطمئن هستید؟')) {
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/bookings/${bookingId}/cancel`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                const errorData = await response.json();
                const errorMessage = errorData.message || `خطا در لغو رزرو: ${response.statusText}`;
                showAlert(errorMessage, 'danger');
                throw new Error(errorMessage);
            }

            showAlert('رزرو با موفقیت لغو شد.', 'success');
            await fetchBookings(); // Refresh list
            await fetchResources(); // Refresh resources status if changed
        } catch (error) {
            console.error('Error cancelling booking:', error);
            // Alert already shown by response.ok check, or can add a generic one here
            // showAlert('خطا در لغو رزرو.', 'danger');
        }
    }

    // --- Event Listeners ---
    filterBookingsBtn.addEventListener('click', fetchBookings);

    // --- Initial Load ---
    fetchResources();
    fetchBookings();
});
