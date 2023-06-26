

function MyUser({ handleAddNewContactClick, userProfilePic, userName }) {

    return (
        <div className="row heading">
            <div className="col-md-3 col-sm-6 col-3 heading-avatar">
                <div className="heading-avatar-icon">
                    <img src={userProfilePic} />
                    {/* <img src="https://bootdey.com/img/Content/avatar/avatar1.png" /> */}

                </div>
            </div>
            

            <div className="col-md-9 col-sm-3 col-1 heading-person-add">
                
                <button
                    type="button"
                    className="btn btn-outline-secondary addUserBtn"
                    data-bs-toggle="modal"
                    data-bs-target="#addUserModal"
                >
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        width={16}
                        height={16}
                        fill="currentColor"
                        className="bi bi-person-plus-fill"
                        viewBox="0 0 16 16"
                    >
                        <path d="M1 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6z" />
                        <path
                            fillRule="evenodd"
                            d="M13.5 5a.5.5 0 0 1 .5.5V7h1.5a.5.5 0 0 1 0 1H14v1.5a.5.5 0 0 1-1 0V8h-1.5a.5.5 0 0 1 0-1H13V5.5a.5.5 0 0 1 .5-.5z"
                        />
                    </svg>
                </button>
            </div>
        </div>

    );

}

export default MyUser;