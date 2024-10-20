import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import {WEBSOCKET} from "../Constant";

class WebSocketService{
    constructor() {
        const socket = new SockJS(WEBSOCKET.getUrl());
        this.stompClient = Stomp.over(socket);
    }

    connectAndSubscribe = (topics, callBack) => {
        this.stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            topics.forEach((topic, index) => {
                this.stompClient.subscribe(topic, (msg) => {
                    if (msg.body) {
                        callBack(msg.body, index)
                    }
                });
            })
        });
    }
}

export default WebSocketService