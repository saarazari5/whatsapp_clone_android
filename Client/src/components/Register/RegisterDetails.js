import DynamicForm from "./DynamicForm.js";
import { useUserUpdate, users } from '../../UserContext.js';
import { useNavigate } from 'react-router-dom';


const formData = {
  username: {
    type: 'text',
    label: '',
    placeholder: 'Enter your Username',
    className: 'form-control',
    validate: (value) => {
      if (!value) {
        return ["Please enter a username"]
      }
      for (let i = 0; i < users.length; i++) {
        if (users[i].username === value) {
          return ["This username is already taken"]
        }
      }
      return [];
    }
  },
  email: {
    type: 'email',
    label: '',
    placeholder: 'Enter your email',
    className: 'form-control',
    validate: (value) => {
      if (!value) {
        return ["Email is required"];
      }
      const regex = /\S+@\S+\.\S+/;
      if (!regex.test(value)) {
        return ["Please enter a valid email address"];
      }
      return [];
    },
  },
  password: {
    type: 'password',
    label: '',
    placeholder: 'Enter your password',
    className: 'form-control',
    validate: (value) => {
      if(!value) {
        return ["password is required"];
      }
      if (value.length < 8) {
        return ["Password must be at least 8 characters long."];
      }
      if (/\s/.test(value)) {
        return ["Password cannot contain whitespace."];
      }
      return [];
    }
  },
  repeatPassword: {
    type: 'password',
    label: '',
    placeholder: 'Repeat your password',
    className: 'form-control',
    validate: (info) => {
      if (!info.password) {
        return ["Please repeat your password"];
      }
      if (info.password !== info.repeatPassword) {
        return ["Passwords do not match. Please try again."];
      }
      if (info.repeatPassword.length < 8) {
        return ["Password must be at least 8 characters long."];
      }
      if (/\s/.test(info.repeatPassword)) {
        return ["Password cannot contain whitespace."];
      }
      return [];
    },
  },
  profilePic: {
    type: 'file',
    label: '',
    placeholder: 'choose a picture',
    className: 'form-control',
    validate: (value) => {
      if (!value) {
        return ["File is required"];
      }
      const allowedTypes = ["png", "jpeg"];
      const fileExtension = value.split('.').pop().toLowerCase();

      if (!allowedTypes.includes(fileExtension)) {
        return ["File type is not allowed"];
      }
      return [];
    },
  },
}

function RegisterDetails() {

  const updateUser = useUserUpdate();
  const navigate = useNavigate();
  
  function userSuccesfullyRegistered() {
    // add a post request to the server to creat the new user!
    // users.push(user);
    // updateUser(user);
    navigate('/');
  }

  return (
    <div className="col-md-9 register-right">
      <div className="tab-content" id="myTabContent">
        <div className="tab-pane fade show active login" id="home" role="tabpanel" aria-labelledby="home-tab">
          <h3 className="register-heading">New user registration</h3>
          <DynamicForm submitProps={{ className: 'btnRegister', title: 'Register' }} formData={formData}  onSuccess= {userSuccesfullyRegistered}  />
        </div>
      </div>
    </div>
  );
}

export default RegisterDetails;