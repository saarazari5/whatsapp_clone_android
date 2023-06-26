
import React , {useState, useContext} from "react";

export const users = [{email: "saarazari5@gmail.com", password: "1"}];

const UserContext = React.createContext()
const UserUpdateContext = React.createContext()

export function useUser() {
    return useContext(UserContext)
}

export function useUserUpdate() {
    return useContext(UserUpdateContext)
}

export function UserProvider({children}) {
    const [currentUser, setCureentUser] = useState({});

    function updateUser(user) {
        setCureentUser(user)
    }
    
    return (
       <UserContext.Provider value={currentUser}>
        <UserUpdateContext.Provider value={updateUser}>
          {children}
        </UserUpdateContext.Provider>
       </UserContext.Provider> 
    );
}