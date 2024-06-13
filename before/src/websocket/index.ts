// src/websocketService.ts
import {baseWs} from "@/constant/config";
import {getToken} from "@/utils/handler-token";

class WebSocketService {
    private socket: WebSocket | null = null;
    private listeners: Array<(message: any) => void> = [];
    private responseCallbacks: { [id: string]: (response: any) => void } = {};
    private reconnectInterval: number;
    private reconnectTimeout: ReturnType<typeof setTimeout> | null = null; // 更新这里
    private isManualClose: boolean = false;

    constructor(private url: string, reconnectInterval: number = 1000) {
        this.reconnectInterval = reconnectInterval;
        this.connect();
    }

    private connect(): void {
        this.socket = new WebSocket(this.url);

        this.socket.onopen = () => {
            console.log('WebSocket connection established');
            if (this.reconnectTimeout) {
                clearTimeout(this.reconnectTimeout);
                this.reconnectTimeout = null;
            }
        };

        this.socket.onmessage = (event: MessageEvent) => {
            const message = JSON.parse(event.data);

            if (message.id && this.responseCallbacks[message.id] && message.type=="api") {
                this.responseCallbacks[message.id](message);
                delete this.responseCallbacks[message.id];
            } else {
                this.listeners.forEach(listener => listener(message));
            }
        };

        this.socket.onclose = () => {
            console.log('WebSocket connection closed');
            if (!this.isManualClose) {
                this.scheduleReconnect();
            }
        };

        this.socket.onerror = (error: Event) => {
            console.error('WebSocket error: ', error);
            this.socket?.close();
        };
    }

    private scheduleReconnect(): void {
        if (this.reconnectTimeout === null) {
            this.reconnectTimeout = setTimeout(() => {
                console.log('Attempting to reconnect...');
                this.connect();
            }, this.reconnectInterval);
        }
    }

    public send(message: any): void {
        if (this.socket && this.socket.readyState === WebSocket.OPEN) {
            this.socket.send(JSON.stringify(message));
        } else {
            console.error('WebSocket is not open. Unable to send message:', message);
            this.connect();
        }
    }

    public addListener(callback: (message: any) => void): void {
        this.listeners.push(callback);
    }

    public removeListener(callback: (message: any) => void): void {
        this.listeners = this.listeners.filter(listener => listener !== callback);
    }

    public sendRequest(request: any): Promise<any> {
        const id = Math.random().toString(36).substring(7); // 生成随机ID
        const message = { id, request };

        return new Promise((resolve) => {
            this.responseCallbacks[id] = resolve;
            this.send(message);
        });
    }

    public close(): void {
        this.isManualClose = true;
        if (this.socket) {
            this.socket.close();
        }
    }
}


const websocketService = new WebSocketService(baseWs+"/api?token="+getToken());
export default websocketService;
