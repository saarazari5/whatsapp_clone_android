import './chatPage.css';
import SideBar from './SideBarComp/SideBar.js';
import ChatWindow from './ChatWindowComp/ChatWindow.js';
import AddNewContactModal from './AddContactModal.js';
import { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { useUser } from '../../UserContext.js';
import whatsappImage from '../../whatsapp_img.png';
import { Link } from 'react-router-dom';
import { addNewContact, fetchContacts, sendNewMessageToServer, getMessages, deleteContact } from './API/ChatPageAPI.js';
import socket from '../../socket.js';

function ChatContainer({ handleLogout }) {
    const [contacts, setContacts] = useState([]);
    const [currentContact, setCurrentContact] = useState(null);
    const user = useUser();


    const handleContacts = async () => {
        const fetchedContacts = await fetchContacts(user);
        fetchedContacts.sort((a, b) => {
            if (a.lastMessage) {
                if (b.lastMessage) {
                    return new Date(b.lastMessage.created) - new Date(a.lastMessage.created)
                }
                return a;
            }
            return a;
        });
        setContacts(fetchedContacts);
    }

    useEffect(() => {
        const MessageReceivedFunc = async (data) => {
            if (!currentContact) {
                handleContacts();
                return;
            }
            if (currentContact.id === data.room) {
                const messages = await getMessages(user, data.room);
                currentContact.messages = messages
                setCurrentContact(currentContact)
            }
            handleContacts()
        }

        socket.on("receive_message", MessageReceivedFunc);
        return () => {
            socket.off("receive_message", null);
        };
    }, [currentContact])

    useEffect(() => {
        const handleNewContact = async (data) => {
            if (data.res.user.username === user.username) {
                await socket.emit('join_room', data.res.id);
                const fetchedContacts = await fetchContacts(user);
                setContacts(fetchedContacts);
            }
        }
        socket.on("add_new_contact", handleNewContact);
        return () => {
            socket.off("add_new_contact", null);
        };
    }, [currentContact])

    useEffect(() => {
        const handleDelete = async (data) => {
            if (user.username === data.deletedContact){
        
                if (!currentContact) {
                    await handleContacts();
                    return;
                }
                const otherUser = currentContact.users.find(u => u.username !== user.username);
                if (data.currentUser === otherUser.username){
                    setCurrentContact(null);
                }
                await handleContacts();
            }
        };
        socket.on("recived_delete", handleDelete);
        return () => {
            socket.off("recived_delete", null);
        };
    }, [contacts]);



    useEffect(() => {
        const f = async () => {
            const fetchedContacts = await fetchContacts(user);
            for (const contact of fetchedContacts) {
                await socket.emit("join_room", contact.id);
            }
            setContacts(fetchedContacts);
        }
        f()
    }, []);


    //********** fixed with API ***************/
    const addContact = async (name) => {
        if (name === user.username){
            alert('You can not add yourself.');
            return;
        }

        const res = await addNewContact(user, { username: name });
        if (!res) {
            alert('No such user exist.');
            return;
        }
        await socket.emit("join_room", res.id);
        const requestedUser = user.username;
        await socket.emit("create_contact", { res, requestedUser });
        const fetchedContacts = await fetchContacts(user);
        setContacts(fetchedContacts);
    };

    const handelPickCurrentContact = async (chatId, clickedContact) => {
        const messages = await getMessages(user, chatId);
        clickedContact.messages = messages;
        setCurrentContact(clickedContact);
    };

    //********** fixed with API ***************/
    const addMessage = async (text) => {
        const res = await sendNewMessageToServer(user, currentContact.id, { "msg": text });
        if (!res) { return }
        const messages = await getMessages(user, currentContact.id);
        currentContact.messages = messages
        setCurrentContact(currentContact)
        await handleContacts()
        await socket.emit("send_message", { room: currentContact.id, msg: res });
    };

    //********** recieved messages NOT EXISTS***************/
    const handelExitChatWindow = (e) => {
        if (e.key === 'Escape') {
            e.preventDefault();
            setCurrentContact(null);
        }
    };

    const handelLogoutResetContact = () => {
        setContacts([]);
        handleLogout();
    };

    
    const onDeletePressed = async (id) => {
        console.log("in delete (ChatContainer -> onDeletePressed)"); // test

        await deleteContact(user, id); //  user = the logedin user, id = currentContact.id

        await handleContacts(); // updating the contacts of the logedin user

        // deleting the the logedin user from the deleted contact's contact list
        const otherUser = currentContact.users.find(u => u.username !== user.username);
        socket.emit("delete_contact", {
            currentUser: user.username,
            deletedContact: otherUser.username,
            room: id
        })
        setCurrentContact(null); // setting the current contact of the logedin user to null
    }



    return (
        <>
            <nav className="navbar sticky-top navbar-expand-lg navbar-dark bg-dark">
                <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarToggler" aria-controls="navbarToggler" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <a className="navbar-brand">
                    <img src={whatsappImage} className="d-inline-block align-top navbar-icon mr-2" alt="ChatApp" loading="lazy"></img>
                    ChatApp
                </a>
                <div className="collapse navbar-collapse" id="navbarToggler">
                    <ul className="navbar-nav mr-auto mt-2 mt-lg-0">

                        <li className={`nav-item nav-Logout`} >
                            <Link className="nav-link btn btn-outline-secondary" to='/' onClick={handelLogoutResetContact}>
                                Logout
                            </Link>
                        </li>

                    </ul>
                </div>
            </nav>
            <div className="container-chat chat-app">
                <div className="row chat-app-inner" onKeyDown={handelExitChatWindow}>
                    <SideBar
                        contacts={contacts}
                        addContact={addContact}
                        handelPickCurrentContact={handelPickCurrentContact}
                        addSentMessage={addMessage}
                        userProfilePic={user.profilePic}
                    />
                    {currentContact ? (
                        <ChatWindow
                            addSentMessage={addMessage}
                            currentContact={currentContact}
                            onDeletePressed={onDeletePressed}
                        />
                    ) : (
                        <div className="conversation col-md-8 col-sm-9 col-0" />
                    )}
                </div>
            </div>


            <AddNewContactModal
                addContact={addContact}
            />
        </>

    );
};

export default ChatContainer;