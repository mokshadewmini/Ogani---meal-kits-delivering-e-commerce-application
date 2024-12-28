


async function loadeFeatuers() {
    const response = await fetch(
            "LoadFeatures"


            );
    if (response.ok) {
        const json = await response.json();


        const categoryList = json.categoryList;
       
        const ingredientList = json.ingredientList;
        console.log(categoryList);
        console.log(ingredientList);
        
        loadSelectOptions("category-select", categoryList, ["id", "name"]);

        loadSelectOptions("ingredients-select", ingredientList, ["id", "name"]);
        
    } else {

    }
}

function loadSelectOptions(selectTagId, list, propertyArray) {
    const selectTag = document.getElementById(selectTagId);
    list.forEach(item => {
        let optionTag = document.createElement("option");
        optionTag.value = item[propertyArray[0]];
        optionTag.innerHTML = item[propertyArray[1]];
        selectTag.appendChild(optionTag);
    });
}




async  function ProductListing() {

    const categorySelectTag = document.getElementById("category-select");
    const ingredientSelectTag = document.getElementById("ingredients-select");
    
    const mealKitTag = document.getElementById("mealKit");
    const caloriesTag = document.getElementById("calories");
    const fatTag = document.getElementById("fat");
    const carbohydrateTag = document.getElementById("carbohydrate");
    const proteinTag = document.getElementById("protein");
    const vitaminsMineralsTag = document.getElementById("vitamins-minerals");
    const descriptionTag = document.getElementById("description");
    const priceTag = document.getElementById("price");
    const qtyTag = document.getElementById("qty");
    const img1Tag = document.getElementById("img1");
    const img2Tag = document.getElementById("img2");
    const img3Tag = document.getElementById("img3");
    const showMessage = document.getElementById("message");
//    console.log("dddd");

    const data = new FormData();
    data.append("categoryId", categorySelectTag.value);
    data.append("ingredient", ingredientSelectTag.value);
    data.append("mealKit", mealKitTag.value);
    data.append("calories", caloriesTag.value);
    data.append("fat", fatTag.value);
    data.append("carbohydrate", carbohydrateTag.value);
    data.append("protein", proteinTag.value);
    data.append("vitaminsMinerals", vitaminsMineralsTag.value);
    data.append("description", descriptionTag.value);
    data.append("price", priceTag.value);
    data.append("qty", qtyTag.value);
    data.append("img1", img1Tag.files[0]);
    data.append("img2", img2Tag.files[0]);
    data.append("img3", img3Tag.files[0]);
    
    const response = await fetch(
            "ProductListing",
            {
                method: "POST",
                body: data
            }
    );

  if (response.ok) {
    const json = await response.json();
    
    if (json.success) {
        // Reset the form fields
        categorySelectTag.value = 0;
        ingredientSelectTag.value = 0;
        mealKitTag.value = "";
        caloriesTag.value = "";
        fatTag.value = "";
        carbohydrateTag.value = "";
        proteinTag.value = "";
        vitaminsMineralsTag.value = "";
        descriptionTag.value = "";
        priceTag.value = "";
        qtyTag.value = 1;
        img1Tag.value = null;
        img2Tag.value = null;
        img3Tag.value = null;

        // Display success message using SweetAlert2
        Swal.fire({
            title: 'Success!',
            text: json.content,
            icon: 'success',
            confirmButtonText: 'OK'
        });

    } else {
        // Display error message using SweetAlert2
        Swal.fire({
            title: 'Oops...',
            text: json.content,
            icon: 'error',
            confirmButtonText: 'OK'
        });
    }

} else {
    // Display failure alert if the response is not OK
    Swal.fire({
        title: 'Error!',
        text: 'Please try again later!',
        icon: 'error',
        confirmButtonText: 'OK'
    });
}
}