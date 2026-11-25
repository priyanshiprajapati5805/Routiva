

// =====================================
// 1. LOGIN PAGE
// =====================================


const loginForm = document.getElementById("loginForm");

if (loginForm) {
    loginForm.addEventListener("submit", async function(event) {
        event.preventDefault();

        const email = document.getElementById("email").value.trim().toLowerCase();
        const password = document.getElementById("password").value;

        if (!email || !password) {
            alert("Please enter email and password.");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/api/passengers/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    email: email,
                    password: password
                })
            });

            const result = await response.json();

            if (!response.ok) {
                alert(result.message || "Invalid email or password.");
                return;
            }

            localStorage.setItem("rideLoopCurrentUser", JSON.stringify(result));
            console.log("Logged-in User:", result);

            alert("Login successful! Welcome to Routivaa.");
            window.location.href = "index.html";

        } catch (error) {
            console.error("Login Error:", error);
            alert("Unable to connect to server. Please check whether Backend is running.");
        }
    });
}




// =====================================
// 2. SIGNUP PAGE - SPRING BOOT API
// =====================================

const signupForm = document.getElementById("signupForm");

if (signupForm) {

    signupForm.addEventListener("submit", async function(event) {

        event.preventDefault();

        const name = document.getElementById("fullname").value.trim();
        const email = document.getElementById("signupEmail").value.trim().toLowerCase();
        const phone = document.getElementById("phone").value.trim();
        const password = document.getElementById("signupPassword").value;
        const confirmPassword = document.getElementById("confirmPassword").value;

        if (!name || !email || !phone || !password || !confirmPassword) {
            alert("Please fill all the fields.");
            return;
        }

        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            alert("Please enter a valid email address.");
            return;
        }

        if (!/^[0-9]{10}$/.test(phone)) {
            alert("Please enter a valid 10-digit phone number.");
            return;
        }

        if (password.length < 6) {
            alert("Password must contain at least 6 characters.");
            return;
        }

        if (password !== confirmPassword) {
            alert("Password and Confirm Password do not match!");
            return;
        }

        const newPassenger = {
            name: name,
            email: email,
            phone: phone,
            password: password
        };

        try {

            const response = await fetch("http://localhost:8080/api/passengers", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(newPassenger)
            });

            if (!response.ok) {
                throw new Error("Registration failed. Please try again.");
            }

            const savedPassenger = await response.json();

            const currentUser = {
                id: savedPassenger.id,
                name: savedPassenger.name,
                email: savedPassenger.email,
                phone: savedPassenger.phone
            };

            localStorage.setItem("rideLoopCurrentUser", JSON.stringify(currentUser));


            alert("Signup successful! Your account has been created.");

            window.location.href = "index.html";

        } catch (error) {

            console.error("Signup Error:", error);

            alert("Unable to connect to server. Please check whether Backend is running.");

        }

    });

}



// =====================================
// 3. SEARCH RIDE PAGE
// =====================================

const searchRideForm = document.getElementById("searchRideForm");

if (searchRideForm) {

    searchRideForm.addEventListener("submit", function(event) {

        event.preventDefault();

        const from = document.getElementById("from").value.trim();
        const to = document.getElementById("to").value.trim();
        const travelDate = document.getElementById("travelDate").value;
        const seats = document.getElementById("seats").value;

        if (from.toLowerCase() === to.toLowerCase()) {
            alert("Starting point and destination cannot be the same.");
            return;
        }

        const params = new URLSearchParams({
            from: from,
            to: to,
            date: travelDate,
            seats: seats
        });

        window.location.href = "rides.html?" + params.toString();

    });

}



// =====================================
// 4. AVAILABLE RIDES - BACKEND INTEGRATION
// =====================================

const ridesPage = document.querySelector(".available-section");

if (ridesPage) {

    const params = new URLSearchParams(window.location.search);

    const from = params.get("from");
    const to = params.get("to");
    const date = params.get("date");
    const seats = params.get("seats");

    const ridesList = document.getElementById("ridesList");

    if (from && to && date && seats) {

        document.getElementById("displayFrom").textContent = from;
        document.getElementById("displayTo").textContent = to;

        document.getElementById("displayDate").textContent =
            new Date(date + "T00:00:00").toLocaleDateString("en-IN");

        document.getElementById("displaySeats").textContent =
            seats + (seats === "1" ? " Seat" : " Seats");

        loadAvailableRides();

    } else {
        ridesList.innerHTML = "<p>Please search for a ride first.</p>";
    }

    async function loadAvailableRides() {

        ridesList.innerHTML = "<p>Loading available rides...</p>";

        try {

            const response = await fetch(
                `http://localhost:8080/api/rides/search?pickupLocation=${encodeURIComponent(from)}&dropLocation=${encodeURIComponent(to)}&date=${date}&seats=${seats}`
            );

            if (!response.ok) {
                throw new Error("Unable to search rides.");
            }

            const rides = await response.json();

            if (rides.length === 0) {
                ridesList.innerHTML = `
                    <div class="ride-card">
                        <h2>No Rides Found</h2>
                        <p>No rides are available for your search.</p>
                    </div>`;
                return;
            }

            ridesList.innerHTML = rides.map(ride => {

                const driverName = ride.driver?.name || "Driver";
                const vehicle = ride.driver?.vehicleModel || "Personal Car";

                const rideTime = new Date(ride.rideDateTime)
                    .toLocaleTimeString("en-IN", {
                        hour: "2-digit",
                        minute: "2-digit"
                    });

                return `
                    <div class="ride-card">

                        <div class="ride-top">
                            <div>
                                <h2>${driverName}</h2>
                                <p>${vehicle} • Personal Car</p>
                            </div>
                            <span class="ride-status">Available</span>
                        </div>

                        <div class="ride-details">

                            <div>
                                <span>Departure</span>
                                <h3>${rideTime}</h3>
                            </div>

                            <div>
                                <span>Available Seats</span>
                                <h3>${ride.availableSeats} Seats</h3>
                            </div>

                            <div>
                                <span>Price / Seat</span>
                                <h3>₹${ride.pricePerSeat}</h3>
                            </div>

                        </div>

                        <button class="book-button"
                            onclick="bookRide(${ride.id})">
                            Book Ride
                        </button>

                    </div>
                `;

            }).join("");

        } catch (error) {

            console.error("Search Error:", error);

            ridesList.innerHTML = `
                <div class="ride-card">
                    <h2>Unable to Load Rides</h2>
                    <p>Please check whether Backend is running.</p>
                </div>`;

        }
    }

    // =====================================
    // BOOK RIDE - BACKEND API
    // =====================================

    window.bookRide = async function(rideId) {

        const currentUser = JSON.parse(
            localStorage.getItem("rideLoopCurrentUser") || "null"
        );

        if (!currentUser || !currentUser.id) {
            alert("Please login again before booking.");
            window.location.href = "login.html";
            return;
        }

        const numberOfSeats = Number(seats);

        if (!Number.isInteger(numberOfSeats) || numberOfSeats <= 0) {
            alert("Invalid seat selection.");
            return;
        }

        if (!confirm("Are you sure you want to book this ride?")) {
            return;
        }

        try {

            const response = await fetch(
                `http://localhost:8080/api/bookings?rideId=${rideId}&passengerId=${currentUser.id}&numberOfSeats=${numberOfSeats}`,
                {
                    method: "POST"
                }
            );

            if (!response.ok) {
                const errorMessage = await response.text();
                throw new Error(errorMessage || "Booking failed.");
            }

            const booking = await response.json();

            alert(
                "Ride booked successfully!\n" +
                "Booking ID: " + booking.id +
                "\nTotal Amount: ₹" + booking.totalAmount
            );

            await loadAvailableRides();

        } catch (error) {

            console.error("Booking Error:", error);
            alert("Booking failed: " + error.message);

        }
    };

}



// =====================================
// 5. BOOKING PAGE
// =====================================

const bookingForm = document.getElementById("bookingForm");

if (bookingForm) {

    bookingForm.addEventListener("submit", function(event) {

        event.preventDefault();

        const name = document.getElementById("passengerName").value.trim();
        const email = document.getElementById("passengerEmail").value.trim();
        const phone = document.getElementById("passengerPhone").value.trim();
        const seats = document.getElementById("bookingSeats").value;

        if (name === "" || email === "" || phone === "" || seats === "") {
            alert("Please fill all the fields.");
            return;
        }

        if (!/^[0-9]{10}$/.test(phone)) {
            alert("Please enter a valid 10-digit phone number.");
            return;
        }

        alert(
            "Booking Details\n\n" +
            "Name: " + name +
            "\nEmail: " + email +
            "\nPhone: " + phone +
            "\nSeats: " + seats +
            "\n\nBooking form submitted successfully!"
        );

    });

}


// =====================================
// RIDELOOP - WELCOME PAGE
// =====================================

const signupButton = document.querySelector(".welcome-signup");
const loginButton = document.querySelector(".welcome-login");

// Sign Up Button
if (signupButton) {
    signupButton.addEventListener("click", function () {
        window.location.href = "signup.html";
    });
}

// Login Button
if (loginButton) {
    loginButton.addEventListener("click", function () {
        window.location.href = "login.html";
    });
}


// =====================================
//  AUTHENTICATION PAGE PROTECTION
// =====================================

const currentPage = window.location.pathname.split("/").pop();

const publicPages = [
    "welcome.html",
    "login.html",
    "signup.html"
];

const loggedInUser = localStorage.getItem("rideLoopCurrentUser");

if (!publicPages.includes(currentPage) && !loggedInUser) {
    alert("Please Login or Sign Up first.");
    window.location.href = "welcome.html";
}

// =====================================
// ABOUT US PAGE JAVASCRIPT
// =====================================

// 1. FAQ Accordion
const faqItems = document.querySelectorAll(".faq-item");

faqItems.forEach((item) => {

    item.addEventListener("toggle", function () {

        if (this.open) {

            faqItems.forEach((otherItem) => {

                if (otherItem !== this) {
                    otherItem.open = false;
                }

            });

        }

    });

});


// 2. Smooth Scrolling
const aboutLinks = document.querySelectorAll(
    '.about-page a[href^="#"]'
);

aboutLinks.forEach((link) => {

    link.addEventListener("click", function (event) {

        const targetId = this.getAttribute("href");

        const targetElement = document.querySelector(targetId);

        if (targetElement) {

            event.preventDefault();

            targetElement.scrollIntoView({
                behavior: "smooth"
            });

        }

    });

});


// =====================================
//  MY RIDES - BACKEND INTEGRATION
// =====================================

const upcomingBookings = document.getElementById("upcomingBookings");

if (upcomingBookings) {

    async function loadMyBookings() {

        const loadingMessage = document.getElementById("loadingMessage");

        const currentUser = JSON.parse(
            localStorage.getItem("rideLoopCurrentUser")
        );

        if (!currentUser || !currentUser.id) {
            loadingMessage.textContent = "Please login again to view your bookings.";
            return;
        }

        try {

            const response = await fetch(
                `http://localhost:8080/api/bookings/passenger/${currentUser.id}`
            );

            if (!response.ok) {
                throw new Error("Unable to load bookings.");
            }

            const bookings = await response.json();

            loadingMessage.style.display = "none";

            document.getElementById("totalRides").textContent =
                bookings.length;

            const upcoming = [];
            const completed = [];
            const cancelled = [];

            const now = new Date();

            bookings.forEach(booking => {

                const status = (booking.bookingStatus || "").toUpperCase();
                const rideDate = new Date(booking.ride.rideDateTime);

                if (status === "CANCELLED") {
                    cancelled.push(booking);
                } else if (status === "COMPLETED" || rideDate < now) {
                    completed.push(booking);
                } else {
                    upcoming.push(booking);
                }

            });

            document.getElementById("upcomingRides").textContent =
                upcoming.length;

            document.getElementById("completedRides").textContent =
                completed.length;

            renderBookings(upcoming, "upcomingBookings", "No Upcoming Rides");

            renderBookings(completed, "completedBookings", "No Completed Rides");

            renderBookings(cancelled, "cancelledBookings", "No Cancelled Rides");

        } catch (error) {

            console.error("My Rides Error:", error);

            loadingMessage.textContent =
                "Unable to load bookings. Please check your backend.";

        }

    }

    function renderBookings(bookings, containerId, emptyMessage) {

        const container = document.getElementById(containerId);

        if (bookings.length === 0) {

            container.innerHTML = `
                <div class="empty-rides">
                    <div class="empty-icon">🚘</div>
                    <h3>${emptyMessage}</h3>
                    <p>Find a ride and start your journey!</p>
                    <a href="search.html" class="find-ride-button">
                        Find a Ride
                    </a>
                </div>
            `;

            return;
        }

        container.innerHTML = bookings.map(booking => {

            const ride = booking.ride;

            const date = new Date(ride.rideDateTime);

            const formattedDate = date.toLocaleDateString("en-IN", {
                day: "2-digit",
                month: "short",
                year: "numeric"
            });

            const formattedTime = date.toLocaleTimeString("en-IN", {
                hour: "2-digit",
                minute: "2-digit"
            });

            return `
                <div class="empty-rides" style="margin-bottom:20px;">

                    <h3>
                        ${ride.pickupLocation} → ${ride.dropLocation}
                    </h3>

                    <p>📅 Date: ${formattedDate}</p>

                    <p>🕒 Time: ${formattedTime}</p>

                    <p>💺 Seats Booked: ${booking.numberOfSeats}</p>

                    <p>💰 Total Amount: ₹${booking.totalAmount}</p>

                    <p>Booking ID: ${booking.id}</p>

                    <p>Status: ${booking.bookingStatus}</p>

                </div>
            `;

        }).join("");

    }

    loadMyBookings();

}


// =====================================
// MY RIDES - TAB SWITCHING
// =====================================

function showRideCategory(category) {

    const categories = [
        "upcoming",
        "completed",
        "cancelled"
    ];

    categories.forEach(item => {

        document.getElementById(item + "Category").style.display =
            item === category ? "block" : "none";

    });

    document.querySelectorAll(".ride-tab").forEach(button => {

        button.classList.remove("active");

    });

    const activeButton = Array.from(
        document.querySelectorAll(".ride-tab")
    ).find(button =>
        button.textContent.trim().toLowerCase() === category
    );

    if (activeButton) {
        activeButton.classList.add("active");
    }

}
