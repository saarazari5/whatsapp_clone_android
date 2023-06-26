

const ReceivedMsg = ({ text, timestamp }) => {
    return (
        <div className="row message-body">
            <div className="col-sm-12 message-main-receiver">
                <div className="receiver">
                    <div className="message-text"> {text} </div>
                    <hr className="message-hr" />
                    <span className="message-time pull-right">
                        {timestamp}
                        <i className="fa fa-clock-o"></i>
                    </span>
                </div>
            </div>
        </div>
    );
};

export default ReceivedMsg;
