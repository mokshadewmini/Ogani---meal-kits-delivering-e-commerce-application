
async function verifyAccount() {

    const DTO = {

        verification: document.getElementById("verificationCode").value,

    };

    const response = await fetch(
            "Verification",
            {
                method: "POST",
                body: JSON.stringify(DTO),
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const json = await response.json();
        if (json.success) {
            window.location = "signIn.html"
        } else {


            document.getElementById("message").innerHTML = json.content;
        }

    
    } else {
        document.getElementById("message").innerHTML = "Please try again later!";
    }
}












