function togglePassword(){

    const password=document.getElementById("password");

    const eye=document.getElementById("eye");

    if(password.type==="password"){

        password.type="text";

        eye.className="bi bi-eye-slash-fill";

    }

    else{

        password.type="password";

        eye.className="bi bi-eye-fill";

    }

}