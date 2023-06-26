import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import whatsappImage from '../../whatsapp_img.png';
import LoginForm from './LoginForm.js';
import ForgotModal from './ForgotModal.js';
import Navbar from '../Navbar/Navbar.js';
import { useUserUpdate } from '../../UserContext.js';

function Login({ authenticate }) {
	const navigate = useNavigate();
	const [passwordVisible, setPasswordVisible] = useState(false);
	const [email, setEmail] = useState('');
	const [password, setPassword] = useState('');
	const [isLoading, setIsLoading] = useState(false);
	const updateUser = useUserUpdate();
	const buttons = [
		{ page: 'home', label: 'Home', link: '/chat' },
		{ page: 'register', label: 'Register', link: '/register' },
	];

	const togglePasswordVisibility = () => {
		setPasswordVisible((prevVisible) => !prevVisible);
	};

	const handleForgotPassword = () => {
		// Show forgot password modal
		const modal = document.getElementById('forgotModal');
		modal.classList.add('show');
		modal.style.display = 'block';
		document.body.classList.add('modal-open');

		// Close the modal when the backdrop is clicked
		modal.addEventListener('click', (event) => {
			if (event.target === modal) {
				modal.classList.remove('show');
				modal.style.display = 'none';
				document.body.classList.remove('modal-open');
			}
		});

		// Close the modal when the close button is clicked
		const closeButton = modal.querySelector('.close');
		closeButton.addEventListener('click', () => {
			modal.classList.remove('show');
			modal.style.display = 'none';
			document.body.classList.remove('modal-open');
		});
	};

	const handlePasswordChange = (e) => {
		setPassword(e.target.value);
	};

	const handleLogin = (e, user) => {
		e.preventDefault();
		setIsLoading(false);
		authenticate();
		updateUser(user);
		navigate('/chat');
	};

	return (
		<>
			<Navbar buttons={buttons} />
			<div className="limiter">
				<div className="container-login">
					<div className="wrap-login">
						<div className="login-pic">
							<img src={whatsappImage} alt="img" />
						</div>
						<hr />
						<LoginForm
							email={email}
							password={password}
							isLoading={isLoading}
							setEmail={setEmail}
							setPassword={setPassword}
							handleLogin={handleLogin}
							handleForgotPassword={handleForgotPassword}
							handlePasswordChange={handlePasswordChange}
							passwordVisible={passwordVisible}
							togglePasswordVisibility={togglePasswordVisibility}
							setIsLoading={setIsLoading}
						/>
					</div>
					{isLoading && (
						<div className="spinner-overlay">
							<div className=" justify-content-center align-items-center">
								<div className="spinner-border" role="status"></div>
								<div id="loading-text">Hold My Beer</div>
							</div>
						</div>
					)}
				</div>
			</div>
			<ForgotModal />
		</>
	);
}

export default Login;
