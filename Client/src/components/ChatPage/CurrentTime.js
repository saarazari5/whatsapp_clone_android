
const CurrentTime = () => {
    const currentTime = new Date();

    const year = currentTime.getFullYear();
    const month = String(currentTime.getMonth() + 1).padStart(2, '0');
    const day = String(currentTime.getDate()).padStart(2, '0');

    let hours = currentTime.getHours();
    const amOrPm = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12 || 12; // Convert to 12-hour format
    const minutes = String(currentTime.getMinutes()).padStart(2, '0');

    const formattedDateTime = `${month}/${day}/${year} ${hours}:${minutes} ${amOrPm}`;

    return <span>{formattedDateTime}</span>;
};

export default CurrentTime;
