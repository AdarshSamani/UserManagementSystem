
import { BrowserRouter, Route, Routes, Navigate } from 'react-router-dom'
import Navbar from './components/common/Navbar'
import LoginPage from './components/auth/LoginPage'
import RegistrationPage from './components/auth/RegistrationPage'
import Footer from './components/common/Footer'
import ProfileComponent from './components/userspage/ProfileComponent'
import UpdateUser from './components/userspage/UpdateUser'
import UserManagementPage from './components/userspage/UserManagementPage'
import UserService from './components/service/UserService'

function App() {

  return (
    <>
      <BrowserRouter>
        <div>
          <Navbar/>
          <div>
            <Routes>
              <Route path='/' element={<LoginPage />} />
              <Route path='/login' element={<LoginPage />} />
              <Route path='/profile' element={<ProfileComponent />} />
              {
                UserService.adminOnly() && (
                  <>
                    <Route path='/register' element={<RegistrationPage />} />
                    <Route path='/admin/user-management' element={<UserManagementPage />} />
                    <Route path='/update-user/:userId' element={<UpdateUser />} />
                  </>
                )
              }
              <Route path='*' element={<Navigate to="/login" />} />
            </Routes>
          </div>
          <Footer />
        </div>
      </BrowserRouter>
    </>
  )
}

export default App
