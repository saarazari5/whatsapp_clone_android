import React from "react";
import { useNavigate } from 'react-router-dom';
import whatsappImage from '../../whatsapp_img.png';

function RegisterHello() {
    const navigate = useNavigate();

    function pressedLogin() {
        console.log("pressedLogin")
        navigate('/');
    }

    return(
    <div className="col-md-3 register-left">
        <img src={whatsappImage} alt="ChatApp"/>
        <h3>Welcome</h3>
        <p>The best ChatApp application</p>
        <div className="d-flex justify-content-center links mb-3">Already registered?</div>
        <a onClick={pressedLogin} className="reg-hello-btnLogin">Login here</a>
    </div>
    );
}

export default RegisterHello;