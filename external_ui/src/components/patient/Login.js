import axios from 'axios';
import {AUTHENTICATION} from "../../ApiConstant";
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import RoutesConstant from "../../RoutesConstant";

function Login(){
    const [userName, setUserName] = useState('')
    const [passWord, setPassWord] = useState('')
    const [labelError, setLabelError] = useState('')
    const navigate = useNavigate();

    function doLogin(){
        const data = {
            userName: userName,
            passWord: passWord
        }
        console.log(data)

        axios.post(AUTHENTICATION.authenticate(), data)
            .then(response => {
                console.log(response.data);
                localStorage.setItem('access-token', response.data.accessToken)
                localStorage.setItem('refresh-token', response.data.refreshToken)
                navigate(RoutesConstant.PATIENT.DASHBOARD)
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