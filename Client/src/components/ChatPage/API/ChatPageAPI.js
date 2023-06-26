export async function getMessages(currentUser, chatId) {
    const res = await fetch('http://localhost:5000/api/Chats/' + chatId +
        '/Messages', {
        'method': 'GET',
        'headers': {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${currentUser.token}`
        },
    });
    if (!res.ok) {
        throw new Error(`Error ${res.status}: ${res.statusText}`);
    }
    const chatDetails = await res.json();
    return chatDetails;
}

export async function sendNewMessageToServer(currentUser, chatId, message) {
    try {
        const request = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${currentUser.token}`
            },
            body: JSON.stringify(message)
        };

        const res = await fetch('http://localhost:5000/api/Chats/' + chatId + '/Messages', request);
        if (res.ok) {
            const result = await res.json();
            const { messageId, created, sender, content } = result;
            return { messageId, created, sender, content };
        }
        else {
            return null
        }
    }
    catch (error) {
        console.error(error);
    }
}

export async function findChat(currentUser, user1, user2) {
    try {
        const request = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${currentUser.token}`
            }
        };
        const res = await fetch(`http://localhost:5000/api/Chats/chat/?user1=${user1}&user2=${user2}`, request);
        if (res.ok) {
            return await res.json();
        } else {
            throw new Error(`Failed to get Chat between: ${user1} and ${user2}`);
        }
    }
    catch (error) {
        console.error(error);
    }
    return null;
}

export async function fetchContacts(currentUser) {
    const res = await fetch('http://localhost:5000/api/Chats', {
        'method': 'GET',
        'headers': {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${currentUser.token}`
        },
    });
    if (!res.ok) {
        throw new Error(`Error ${res.status}: ${res.statusText}`);
    }
    const userContacts = await res.json();
    return userContacts;
}


export async function addNewContact(currentUser, newContact) {
    try {
        const request = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${currentUser.token}`
            },
            body: JSON.stringify(newContact)
        };

        const res = await fetch('http://localhost:5000/api/Chats', request);
        if (res.ok) {
            const data = await res.json();
            const { id, user } = data;
            return { id, user };
        } else {
            return null;
        }
    }
    catch (error) {
        console.error(error);
        return false;
    }
}

export async function deleteContact(currentUser, chatId) {

    console.log("in ChatPageAPI -> deleteContact"); // test

    const res = await fetch('http://localhost:5000/api/Chats/' + chatId, {
        'method': 'DELETE',
        'headers': {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${currentUser.token}`
        },
    });

    console.log(res); // test

    if (res.ok) {
        return true;
    } else {
        return false;
    }
}