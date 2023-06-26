

const Contact = ({ contact, name, badgeCount, messagePreview, timestamp, handelRemoveClick, id, handelPickTisContact, profilePic }) => {

    const handelContactClicked = () => {
        handelPickTisContact(id, contact);
    };



    return (
        <div className="row list-group-item sideBar-body" onClick={handelContactClicked}>
            <div className="sideBar-avatar col-2 col-md-2 col-lg-2">
                <div className="avatar-icon">
                    <img src={profilePic} alt="Avatar" />
                </div>
            </div>
            <div className="sideBar-main col-sm-12 col-10 col-md-10 col-lg-10">
                <div className="row">
                    <div className="sideBar-name col-sm-12 col-md-6 col-lg-7">
                        <span className="name-meta">{name}</span>
                        <br />
                        <small className="message-preview">{messagePreview}</small>
                    </div>
                    <div className="sideBar-time col-md-5 col-lg-4 d-flex align-items-center">
                        <span className="time-meta">{timestamp}</span>
                    </div>
                </div>
            </div>
        </div>

    );
};

export default Contact;