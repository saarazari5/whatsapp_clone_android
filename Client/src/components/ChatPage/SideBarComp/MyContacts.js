import { useRef } from 'react';
import { useState, useEffect } from 'react';
import Contact from '../Contact.js';
import { useUser } from '../../../UserContext.js';


const MyContacts = ({ contacts, addContact, handelPickCurrentContact, addSentMessage }) => {
    const currentUser = useUser();
    const searchInputRef = useRef(null);
    const [searchQuery, setSearchQuery] = useState('');
    const [filteredContacts, setFilteredContacts] = useState([]);
    const [isSearchActive, setIsSearchActive] = useState(false);


    function setDate(created) {
        if(!created) {return }
        const messageDate = new Date(created);
        const currentDate = new Date();

        if (
            messageDate.getFullYear() === currentDate.getFullYear() &&
            messageDate.getMonth() === currentDate.getMonth() &&
            messageDate.getDate() === currentDate.getDate()
        ) {
            // Format time if the message is from the current day
            const formattedTime = messageDate.toLocaleDateString([], {
                hour: '2-digit',
                minute: '2-digit',
                hour12: false,

            });
            return formattedTime;
        } else {
            // Format date if the message is older than the current day
            const formattedDate = messageDate.toLocaleDateString();
            return formattedDate;
        }
    }

    const handleSearchQueryChange = (event) => {
        setSearchQuery(event.target.value);
    };


    const handleKeyDown = (event) => {
        if (event.key === "Enter") {
            event.preventDefault();
            searchInputRef.current.blur(); // Remove focus from the input field
        }
    };

    const handleBlur = () => {
        if (searchQuery === "") {
            setIsSearchActive(false);
            setFilteredContacts(contacts);
        }
    };

    useEffect(() => {
        if (searchQuery === '') {
            setIsSearchActive(false);
            setFilteredContacts(contacts);
        } else {
            const filteredItems = contacts.filter((chat) => {
                const otherUser = chat.users.find(user => user.username !== currentUser.username);
                return otherUser.displayName.toLowerCase().includes(searchQuery.toLowerCase())
              }
            );
            setIsSearchActive(true);
            setFilteredContacts(filteredItems);
        }
    }, [contacts, searchQuery, addContact, addSentMessage]);

  

    return (
        <>
            <div className="row searchBox">
                <div className="col-sm-12 searchBox-inner">
                    <div className="form-group has-feedback">
                        <input
                            id="searchText"
                            ref={searchInputRef}
                            type="text"
                            value={searchQuery}
                            className="form-control"
                            name="searchText"
                            onChange={handleSearchQueryChange}
                            onKeyDown={handleKeyDown}
                            onBlur={handleBlur}
                            placeholder="Search by name"
                        />
                    </div>
                </div>
            </div>

            <div style={{ overflow: 'auto', maxHeight: '600px' }} className="row list-group sideBar" role="tablist">
                {isSearchActive
                    ? filteredContacts.map((item, index) => {
                        const otherUser = item.users.find(user => user.username !== currentUser.username);
                         return <Contact
                            contact={item}
                            key={index}
                            name={otherUser.username}
                            badgeCount={0}
                            messagePreview={item.lastMessage ? item.lastMessage.content : ""}
                            timestamp={setDate(item.lastMessage? item.lastMessage.created : null)}
                            id={item.id}
                            handelPickTisContact={handelPickCurrentContact}
                            profilePic={otherUser.profilePic}
                            
                        />
                    })
                    : contacts.map((item, index) => {
                        const otherUser = item.users.find(user => user.username !== currentUser.username);
                          return <Contact
                            contact={item}
                            key={index}
                            name={otherUser.displayName}
                            badgeCount={0}
                            messagePreview={item.lastMessage ? item.lastMessage.content : ""}
                            timestamp={setDate(item.lastMessage? item.lastMessage.created : null)}
                            id={item.id}
                            handelPickTisContact={handelPickCurrentContact}
                            profilePic={otherUser.profilePic}
                            
                        />
                    })}

            </div>
        </>
    );
};

export default MyContacts;
