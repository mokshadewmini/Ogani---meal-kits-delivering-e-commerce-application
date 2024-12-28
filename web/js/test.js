async function SignUp(){
    
    const dto = {
        fname:document.getElementById("fname").value,
        lname:document.getElementById("lname").value,
    }
    
    const response = await fetch(
            "SignUp",
    {
        method:"POST",
        body: JSON.stringify(dto),
        headers:{
            "Content-Type" : "application/json"
        }
    }
            );
    
    if(response.ok){
        const json = await response.json();
        if(json.success){
            
        }else{
            
        }
    }else{
        
    }
}