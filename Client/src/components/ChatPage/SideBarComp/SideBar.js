import MyContacts from './MyContacts.js';
import MyUser from './MyUser.js';


function SideBar({ contacts, addContact, handleSaveContact, handelPickCurrentContact, addSentMessage, userProfilePic, userName }) {


    return (
        <div className="side col-md-4 col-sm-3 col-12">
            <MyUser
                handleAddNewContactClick={handleSaveContact}

                userProfilePic={userProfilePic} 
                />

            <MyContacts
                contacts={contacts} addContact={addContact} handelPickCurrentContact={handelPickCurrentContact} />
        </div>
    );
}

export default SideBar;