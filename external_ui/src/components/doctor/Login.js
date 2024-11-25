import axios from 'axios';
import {AUTHENTICATION, EMPLOYYEE} from "../../ApiConstant";
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import {SendApiService} from "../../service/SendApiService";

function Login(){
    const [userName, setUserName] = useState('')
    const [passWord, setPassWord] = useState('')
    const [labelError, setLabelError] = useState('')
    const navigate = useNavigate();

    const getEmployeeInfor = () => {
        SendApiService.getRequest(EMPLOYYEE.getLoggedUserInformation(), {}, response => {
            localStorage.setItem('doctor', JSON.stringify(response.data))
        }, error => {

        })
    }

    function doLogin(){
        const data = {
            userName: userName,
            passWord: passWord
        }


        axios.post(AUTHENTICATION.authenticateEmployee(), data)
            .then(response => {
                localStorage.setItem('access-token', response.data.accessToken)
                localStorage.setItem('refresh-token', response.data.refreshToken)
                getEmployeeInfor()

                navigate('/employee/doctor')
            })
            .catch(error => {
                console.log(error.response);
                if(error.response != null)
                    setLabelError('Tài khoản hoặc mật khẩu không chính xác')
            });
    }

    return(
        <>
            <label>Tên đăng nhập</label>
            <input value={userName} onChange={(e) => setUserName(e.target.value)}/>

            <label>Mật khẩu</label>
            <input type='password' value={passWord} onChange={(e) => setPassWord(e.target.value)}/>

            <h1>{labelError}</h1>
            <button onClick={doLogin}>Login</button>
        </>
    )
}

export {Login}