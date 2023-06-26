import { useState } from 'react';

const AddNewContactModal = ({ addContact }) => {

    const [userInput, setUserInput] = useState('');
    const errors = {};

    const handleSubmit = (e) => {
        e.preventDefault();
        if (userInput === '') {
            errors.empty = "You must enter a name."
        } else {
            // setNewContactName(userInput);
            addContact(userInput);
            // setNewContactName('');
            setUserInput('');
        }
    };
    const handleExitAddContactClick = () => {
        // setNewContactName('');
        setUserInput('');
        // setIsAddingNewContact(false);
    };

    const handleInput = (e) => {
        // setNewContactName(e.target.value);
        setUserInput(e.target.value);
    };

    return (
        <div
            className="chat-page modal fade"
            id="addUserModal"
            tabIndex={-1}
            aria-labelledby="addUserModalLabel"
            aria-hidden="true"
        >
            <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-header">
                        <h4 className="modal-title fs-5" id="addUserModalLabel">
                            Add new contact
                        </h4>
                        <button
                            type="button"
                            className="btn btn-close"
                            data-bs-dismiss="modal"
                            aria-label="Close"
                            onClick={handleExitAddContactClick}
                        >
                            <i className="fa fa-close" />
                        </button>
                    </div>
                    <div className="modal-body">
                        <form className="row" onSubmit={handleSubmit}>
                            <div className="col-12">
                                <input
                                    onChange={handleInput}
                                    value={userInput}
                                    type="text"
                                    className="form-control"
                                    placeholder="Contact's identifier"
                                />
                            </div>
                        </form>
                    </div>
                    <div className="modal-footer">
                        <button
                            type="submit"
                            className="btn btn-primary"
                            data-bs-dismiss="modal"
                            onClick={handleSubmit}
                        >
                            Add
                        </button>
                    </div>
                </div>
            </div>
        </div>

    );
};

export default AddNewContactModal;