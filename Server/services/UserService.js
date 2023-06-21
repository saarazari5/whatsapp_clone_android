const User = require('../models/User.js');

const getAllUsers = async (req, res) => {
    try {
        // Get all users from the database
        const users = await User.find();
        res.status(200).json(users);
    } catch (error) {
        res.status(500).json({ error: 'Failed to retrieve users from the database.' });
    }
}

const createUser = async (username, password, displayName, profilePic) => {
    const newUser = new User({
        username: username,
        displayName: displayName,
        password: password,
        profilePic: profilePic
    });
    try {

        await newUser.save();
        console.log('User saved successfully:', newUser);
        return {
            status: 200,
            body: {
                username: username,
                displayName: displayName,
                profilePic: profilePic
            }
        };
    }
    catch (error) {
        // console.log(error);
        console.log("error");
        return {
            title: "conflict",
            status: 409
        }
    }
};

const getUser = async (username) => {
    const dbResult = await User.findOne({ username });
    return dbResult;

}

const getUserById = async (req, res) => {
    try {
        // Get given user from database
        const user = await User.findOne({ _id: req.user._id });
        if (!user) {
            res.status(404).json({ message: 'User not found' });
            return;
        }
        res.status(200).json({ user });
    } catch (error) {
        res.status(500).json({ error: 'Failed to get user from the database' });
    }
}

const updateUser = async (req, res) => {
    const userId = req.params.id;
    const updateData = req.body;

    try {
        // Update the user in the database
        const updatedUser = await User.findByIdAndUpdate(userId, updateData, { new: true });

        if (!updatedUser) {
            // If the user with the given ID is not found
            return res.status(404).json({ message: 'User not found.' });
        }

        res.status(200).json(updatedUser);
    } catch (error) {
        res.status(500).json({ message: 'Failed to update the user' });
    }
}

const deleteUser = async (req, res) => {
    const userId = req.params.id;

    try {
        // Delete the user from the database
        const deletedUser = await User.findByIdAndDelete(userId);

        if (!deletedUser) {
            // If the user with the given ID is not found
            return res.status(404).json({ message: 'User not found' });
        }

        res.status(200).json({ message: 'User deleted successfully' });
    } catch (error) {
        res.status(500).json({ message: 'Failed to delete the user' });
    }
}

module.exports = { getAllUsers, createUser, getUser, getUserById, updateUser, deleteUser };