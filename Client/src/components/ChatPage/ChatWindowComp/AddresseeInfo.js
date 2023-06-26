import React from 'react';

import { useUser } from '../../../UserContext.js';

const AddresseeInfo = ({ currentContact, onDeletePressed }) => {
    const currentUser = useUser();
    const otherUser = currentContact.users.find(user => user.username !== currentUser.username);


    return (
        <div className="row heading">
            <div className="col-sm-2 col-md-1 col-xs-3 heading-avatar">
                <div className="heading-avatar-icon">
                    <img src={otherUser.profilePic} alt="Avatar" />
                </div>
            </div>
            <div className="col-sm-8 col-xs-7 heading-name">
                <span className="name-meta">{otherUser.displayName}</span>
            </div>
            <div style={{ marginTop: '8px' }} className="col-sm-1 col-xs-1 heading-dot pull-right">
                <svg onClick={() => onDeletePressed(currentContact.id)} xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" className="bi bi-trash" viewBox="0 0 16 16">
                    <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5Zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5Zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6Z" />
                    <path d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1ZM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118ZM2.5 3h11V2h-11v1Z" />
                </svg>
            </div>
        </div>
    );
};

export default AddresseeInfo;
