// ================= MODALS =================

function openLogin() {
    document.getElementById("loginModal").style.display = "flex";
}

function openSignup() {
    document.getElementById("signupModal").style.display = "flex";
}

function closeModal(id) {
    document.getElementById(id).style.display = "none";
}


// ================= SWITCH MODALS =================

function switchModal() {
    closeModal("loginModal");
    openSignup();
}

function switchToLogin() {
    closeModal("signupModal");
    openLogin();
}


// ================= LOGIN =================

function login() {

    const email = document.querySelector(
        '#loginModal input[type="email"]'
    ).value;

    const password = document.querySelector(
        '#loginModal input[type="password"]'
    ).value;


    // Check empty fields
    if (email === "" || password === "") {

        showToast(
            "Please enter email and password ⚠️"
        );

        return;
    }


    // Get saved student
    const savedStudent =
        localStorage.getItem("skillswapStudent");


    // If account doesn't exist
    if (!savedStudent) {

        showToast(
            "Account not found. Please sign up first."
        );

        return;
    }


    const student =
        JSON.parse(savedStudent);


    // Check login details
    if (
        email !== student.email ||
        password !== student.password
    ) {

        showToast(
            "Incorrect email or password ❌"
        );

        return;
    }


    // Save login status
    localStorage.setItem(
        "skillswapLoggedIn",
        "true"
    );


    showToast(
        "Login successful! Welcome back 👋"
    );


    setTimeout(() => {

        window.location.href =
            "dashboard.html";

    }, 1000);

}

// ================= SIGNUP =================

function signup() {

    const name = document.querySelector(
        '#signupModal input[type="text"]'
    ).value;

    const email = document.querySelector(
        '#signupModal input[type="email"]'
    ).value;

    const password = document.querySelector(
        '#signupModal input[type="password"]'
    ).value;


    // Check empty fields
    if (name === "" || email === "" || password === "") {

        showToast("Please fill all the fields ⚠️");

        return;
    }


    // Save student information
    const student = {

        name: name,
        email: email,
        password: password

    };


    localStorage.setItem(
        "skillswapStudent",
        JSON.stringify(student)
    );


    showToast(
        "Account created successfully! 🎉"
    );


    setTimeout(() => {

        closeModal("signupModal");

        openLogin();

    }, 1000);

}

// ================= CONNECT =================

function sendRequest(name) {

    showToast("Connection request sent to " + name + " 🤝");

}


// ================= SEARCH =================

function searchSkills() {

    const search =
        document.getElementById("skillSearch")
        .value
        .toLowerCase();

    const cards =
        document.querySelectorAll(".student-card");

    cards.forEach(card => {

        const text =
            card.innerText.toLowerCase();

        if (text.includes(search)) {
            card.style.display = "block";
        } else {
            card.style.display = "none";
        }

    });

}


// ================= FILTER =================

function filterSkill(category) {

    const cards =
        document.querySelectorAll(".student-card");

    const buttons =
        document.querySelectorAll(".category");

    buttons.forEach(button => {
        button.classList.remove("active");
    });

    event.target.classList.add("active");


    cards.forEach(card => {

        if (
            category === "all" ||
            card.dataset.category === category
        ) {

            card.style.display = "block";

        } else {

            card.style.display = "none";

        }

    });

}


// ================= SCROLL =================

function scrollToSection(id) {

    document
        .getElementById(id)
        .scrollIntoView({
            behavior: "smooth"
        });

}


// ================= TOAST =================

function showToast(message) {

    const toast =
        document.getElementById("toast");

    const toastMessage =
        document.getElementById("toastMessage");

    toastMessage.innerText = message;

    toast.classList.add("show");

    setTimeout(() => {

        toast.classList.remove("show");

    }, 3000);

}


// ================= CLOSE MODAL OUTSIDE =================

window.addEventListener("click", function(event) {

    const loginModal =
        document.getElementById("loginModal");

    const signupModal =
        document.getElementById("signupModal");

    if (event.target === loginModal) {
        closeModal("loginModal");
    }

    if (event.target === signupModal) {
        closeModal("signupModal");
    }

});