import { useParams, useNavigate } from "react-router-dom";

import React, { useState, useEffect } from 'react'
import UserService from "../service/UserService";

const UpdateUser = () => {
    const navigate = useNavigate() 

    const params = useParams() 
    const userId = params.userId
    // console.log(userId)

    const [userData, setUserData] = useState({
        name:'',
        email:'',
        role:'',
        city:''
    })

    useEffect(() => {
        fetchUserDataById(userId)
    }, [userId])
    
    const fetchUserDataById = async (userId) => {
        try {
            const token = localStorage.getItem('token')
            const response = await UserService.getUserById(userId,token)
            const {name,email,role,city} = response.ourUsers
            setUserData({name,email,role,city})
        } catch (error) {
            console.error('Error while fetching user data: ',error)
        }
    }

    const handleInputChange = (e) => {
        const {name,value} = e.target
        setUserData((prevUserdata) => ({
            ...prevUserdata,
            [name]:value
        }))
    }

    const handleSubmit = async(e) => {
        e.preventDefault()
        try {
            const token = localStorage.getItem('token')
            const response = await UserService.updateUser(userId,userData,token)
            navigate('/admin/user-management')
        } catch (error) {
            console.error('Error Updating User Profile: ',error)
            alert(error)
        }
    }
  return (
    <div className="auth-container">
        <h2>Update User</h2>
        <form onSubmit={handleSubmit}>
            <div className="form-group">
                <label>Name:</label>
                <input type="text" name="name" value={userData.name} onChange={handleInputChange} />
            </div>
            <div className="form-group">
                <label>Email:</label>
                <input type="email" name="email" value={userData.email} onChange={handleInputChange} />
            </div>
            <div className="form-group">
                <label>Role:</label>
                <input type="text" name="role" value={userData.role} onChange={handleInputChange} />
            </div>
            <div className="form-group">
                <label>City:</label>
                <input type="text" name="city" value={userData.city} onChange={handleInputChange} />
            </div>
            <button type="submit">Update</button>
        </form>
    </div>
  )
}

export default UpdateUser