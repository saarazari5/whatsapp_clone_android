import { useRef, useState, useEffect } from "react";
import AddresseeInfo from "./AddresseeInfo.js";
import ReceivedMsg from "./RecivedMsg.js";
import MyMsg from "./MyMsg.js";
import { useUser } from '../../../UserContext.js';


const MsgSender = ({ addSentMessage }) => {
    const [userInput, setUserInput] = useState('');

    const handleInput = (e) => {
        setUserInput(e.target.value);
    };

    const handleSend = () => {
        if (userInput !== '') {
            addSentMessage(userInput);
            setUserInput('');
        }
    };

    const handleKeyDown = (event) => {
        if (event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault();
            handleSend();
        }
    };

    return (
        <div className="row reply">
            <div className="col-sm-1 col-xs-1 reply-emojis" id="emoji-button">
                <i className="fa fa-smile-o fa-2x" />
            </div>
            <div className="col-sm-9 col-xs-9 reply-main">
                <textarea
                    className="form-control"
                    rows={1}
                    id="comment"
                    onChange={handleInput}
                    onKeyDown={handleKeyDown}
                    value={userInput}
                />
            </div>
            <div className="col-sm-1 col-xs-1 reply-recording">
                <i className="fa fa-microphone fa-2x" aria-hidden="true" />
            </div>
            <div className="col-sm-1 col-xs-1 reply-send" onClick={handleSend}>
                <i className="fa fa-send fa-2x" aria-hidden="true" />
            </div>
        </div>

    );
};




function ChatWindow({ addSentMessage, currentContact, onDeletePressed }) {

    function setDate(created) {
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

    const currentUser = useUser();
    const chatWindowRef = useRef(null);



    const scrollToBottom = () => {
        chatWindowRef.current?.scrollIntoView({ behavior: 'smooth' })
  };

    useEffect(() => {
       scrollToBottom()
    }, [currentContact, currentContact.messages]);


    useEffect(() => {
        scrollToBottom()
     }, []);
     
    return (
        <div className="conversation col-md-8 col-sm-9 col-0"  >
            <AddresseeInfo currentContact={currentContact} onDeletePressed={onDeletePressed}/>
            <div style={{ overflow: 'auto', maxHeight: '900px' }}>
                {currentContact.messages.map((message, index) => {
                    if (message.sender.username === currentUser.username) {
                        return (
                            <MyMsg key={index} text={message.content} timestamp={setDate(message.created)} />
                        );
                    } else {
                        return (
                            <ReceivedMsg key={index} text={message.content} timestamp={setDate(message.created)} />
                        );
                    }
                })}

                <div ref={chatWindowRef} />
            </div>
            <MsgSender addSentMessage={addSentMessage} />
        </div>
    );
};

export default ChatWindow;