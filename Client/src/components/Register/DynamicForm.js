import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useUserUpdate, users } from '../../UserContext.js';
import defaultPic from '../../cameraPicture.png';



const DynamicForm = ({ submitProps, formData, onSuccess }) => {
  const [formValues, setFormValues] = useState({});
  const [formErrors, setFormErrors] = useState({});
  const [previewSrc, setPreviewSrc] = useState(defaultPic);
  const [picFile, setpicFile] = useState(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    if (name === "profilePic") {
      const maxFileSize = 50 * 1024; // 50KB in bytes
      const file = e.target.files[0];
      if (file && file.size > maxFileSize) { // File size exceeds the limit
        
        setpicFile(true);
      }else{
        setpicFile(false);
      }

      const reader = new FileReader();
      reader.onload = (event) => {
        setPreviewSrc(reader.result);
      };
      reader.readAsDataURL(file);
    }
    setFormValues({ ...formValues, [name]: value });
    setFormErrors({ ...formErrors, [name]: '' });
  };

  const validateForm = () => {
    const errors = {};
    Object.keys(formData).forEach((fieldName) => {
      const fieldConfig = formData[fieldName]; // jason of user info
      const fieldValue = formValues[fieldName]; // what the user entered
      const fieldValidation = fieldConfig.validate; // validtion func

      if (fieldValidation) {
        var fieldErrors = [];
        if (fieldName === "repeatPassword") {
          fieldErrors = fieldValidation({ password: formValues["password"], repeatPassword: fieldValue });
        } else {
          fieldErrors = fieldValidation(fieldValue);
        }
        if (fieldErrors.length > 0) {
          errors[fieldName] = fieldErrors;
        }
      }
    });
    return errors;
  };

  const renderField = (fieldName, fieldConfig) => {
    const errorMessage = formErrors[fieldName] || [];
    const inputclassName = `form-input form-control form-input-${fieldConfig.type} ${fieldConfig.className} ${errorMessage.length > 0 ? "form-group-error" : ""}`;
    const divclassName = `form-group validate-input`;
    return (
      <div className={divclassName} key={fieldName}>
        <input
          type={fieldConfig.type}
          id={fieldName}
          name={fieldName}
          value={formValues[fieldName] || ''}
          onChange={handleInputChange}
          className={inputclassName}
          placeholder={fieldConfig.placeholder}
        />
        <div key={errorMessage} className="invalid-input">
          {errorMessage}
        </div>
        {(previewSrc && fieldName === "profilePic" && errorMessage.length === 0) && <img src={previewSrc} alt="Preview" />}
      </div>
    );
  };

  async function register(userData) {
    try {

      const req = {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
      };
      const res = await fetch('http://localhost:5000/api/Users', req);
      console.log(res); // test
      if (res.status === 200) {
        return true;
      }
      if (res.status == 413){
      }
      return false;

    } catch (e) {
      console.log(e);
    }
  }

  const handleSubmit = async (event) => {
    event.preventDefault();
    let errors = validateForm(formData);

    if(picFile){
      errors["profilePic"] = "Profile picture is too big";
       setFormErrors(errors);
    }

    if (Object.keys(errors).length === 0) {

      let userData = {
        "username": formValues['email'],
        "password": formValues['password'],
        "displayName": formValues['username'],
        "profilePic": previewSrc,
      }
      const tmp = await register(userData);
      if (tmp) {
        onSuccess();
      } else {
        errors["email"] = "Email already in use";
        setFormErrors(errors);
      }
    } else {
      setFormErrors(errors);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="row register-form" >
      <div className="col-md-12">
        {Object.keys(formData).map((fieldName) =>
          renderField(fieldName, formData[fieldName])
        )}
        <button className={submitProps.className} type="submit" value="Register"><i className="fas fa-user-plus mr-3"></i>{submitProps.title}</button>
      </div>
    </form>
  );
};

export default DynamicForm;
