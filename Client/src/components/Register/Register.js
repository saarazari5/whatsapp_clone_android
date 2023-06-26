import RegisterHello from "./RegisterHello.js";
import RegisterDetails from "./RegisterDetails.js";
import "./registerPage.css"
import Navbar from "../Navbar/Navbar.js";


function Register() {
    const buttons = [
        { page: 'home', label: 'Home', link: '../chat' },
        { page: 'login', label: 'Login', link: '../' },
    ];
    return (
        <>
            <Navbar buttons = {buttons}/>
            <div className="register-page container register mb-3 mt-3">
                <div className="row">
                         <RegisterHello />
                         <RegisterDetails/>
                </div>
            </div>
        </>
    );
}

export default Register;