async function loadProduct() {

    const parameters = new URLSearchParams(window.location.search);

    if (parameters.has("pid")) {
        const pid = parameters.get("pid");
        const response = await fetch("LoadSingleProduct?pid=" + pid);

        if (response.ok) {
            const json = await response.json();
            const id = json.product.id;
            document.getElementById("image1").src = "product-images/" + id + "/2.jpg";
            document.getElementById("thumbnail1").href = "product-images/" + id + "/2.jpg";
            //  document.getElementById("leftimage1").src = "product-images/" + id + "/2.jpg";
            document.getElementById("image2").src = "product-images/" + id + "/4.jpg";
            document.getElementById("thumbnail2").href = "product-images/" + id + "/4.jpg";
            //  document.getElementById("leftimage2").src = "product-images/" + id + "/4.jpg";
            document.getElementById("image3").src = "product-images/" + id + "/pend.jpg";
            document.getElementById("thumbnail3").href = "product-images/" + id + "/pend.jpg";
            //document.getElementById("leftimage3").src = "product-images/" + id + "/pend.jpg";

            document.getElementById("title").innerHTML = json.product.name;
            document.getElementById("title-1").innerHTML = json.product.name;
            document.getElementById("description").innerHTML = json.product.description;
            document.getElementById("description-1").innerHTML = json.product.description;
            document.getElementById("product-stock").innerHTML = json.product.qty;
            //  document.getElementById("product-published-on").innerHTML = json.product.date_time;

            document.getElementById("product-price").innerHTML = new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }
            ).format(json.product.price);

            document.getElementById("product-category").innerHTML = json.product.category.name;

            $(document).ready(function () {
                $('.product__details__pic__slider').owlCarousel({
                    items: 1,
                    loop: false, // Disable looping
                    margin: 10,
                    nav: false, // Disable navigation
                    dots: false // Disable dots
                });

                // Optionally, you can stop carousel animations and transitions
                $('.owl-carousel').trigger('stop.owl.autoplay');
            });







        } else {
            window.location = "index.html";
        }

    } else {
        window.location = "index.html";
    }

}

async function addToCart() {

    // Get the full URL
    const url = window.location.href;

// Create a URL object
    const urlParams = new URLSearchParams(window.location.search);

// Get a specific parameter value by its name
    const pid = urlParams.get('pid');
    const qty = document.getElementById("qty").value;
    
    console.log(pid);
    console.log(qty);

    const response = await fetch("AddToCart?pid=" + pid + "&qty=" + qty);



    if (response.ok) {
        const json = await response.json();
        if (json.success) {
            Swal.fire({
            title: 'Success!',
            text: json.content,
            icon: 'success',
            confirmButtonText: 'OK'
        });
        } else {
            Swal.fire({
        title: 'Error!',
        text: 'Something went wrong',
        icon: 'error',
        confirmButtonText: 'OK'
    });
        }
    } else {
       Swal.fire({
        title: 'Error!',
        text: 'Please try again later!',
        icon: 'error',
        confirmButtonText: 'OK'
    });
    }

}