import './Navbar.css';
import whatsappImage from '../../whatsapp_img.png';
import { Link } from 'react-router-dom';

function Navbar({ buttons }) {
    return (
        <nav className="navbar sticky-top navbar-expand-lg navbar-dark bg-dark">
            <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarToggler" aria-controls="navbarToggler" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>
            <a className="navbar-brand">
                <img src={whatsappImage} className="d-inline-block align-top navbar-icon mr-5" alt="ChatApp" loading="lazy"></img>
                ChatApp
            </a>
            <div className="collapse navbar-collapse" id="navbarToggler">
                <ul className="navbar-nav mr-auto mt-2 mt-lg-0">
                    {buttons && buttons.map((button, index) => (
                        <li className={`nav-item nav-${button.page}`} key={index}>
                            <Link className="nav-link btn btn-outline-secondary" to={button.link}>
                                {button.label}
                            </Link>
                        </li>
                    ))}
                </ul>
            </div>
        </nav>
    );
}

export default Navbar;