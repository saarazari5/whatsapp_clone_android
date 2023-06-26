import React from 'react';
import ModalForm from './ModalForm.js';

function ForgotModal() {
    return (
		// < !--Forgot Password Modal HTML-- >
        <div id="forgotModal" className="modal fade dark-modal" tabIndex="-1" aria-labelledby="forgotModalLabel" aria-hidden="true">
            <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title" id="forgotModalLabel">Reset Password</h5>
                        <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <ModalForm />
                </div>
            </div>
        </div>
	);
}

export default ForgotModal;
