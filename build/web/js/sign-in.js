
async function signIn(){
    
    const User_DTO = {
        
        email:document.getElementById("email").value,
        password:document.getElementById("password").value,
        
    };
    
    const response = await fetch(
            "SignIn",
    {
        method:"POST",
        body: JSON.stringify(User_DTO),
        headers:{
            "Content-Type":"application/json"
        }
    }
            );
     
    if(response.ok){
        const json = await response.json();
        if(json.success){
            window.location = "index.html"
            
        }else{
            
            if(json.content==="UnVerified"){
                window.location = "verify-account.html"
            }else{
                 document.getElementById("message").innerHTML = json.content; 
            }
         
        }
    }else{
         document.getElementById("message").innerHTML = "Please try again later!";
    }
}









