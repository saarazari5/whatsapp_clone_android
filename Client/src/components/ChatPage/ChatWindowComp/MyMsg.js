

const MyMsg = ({ text, timestamp}) => {
    return (
        <div className="row message-body">
            <div className="col-sm-12 message-main-sender">
                <div className="sender">
                    <div className="message-text"> {text} </div>
                    <hr className="message-hr" />
                    <span className="message-time pull-right">
                        {timestamp}
                        <i className="fa fa-clock-o"></i>
                    </span>
                    <hr className="message-hr" />
                </div>
            </div>
        </div>
    );
};

export default MyMsg;
