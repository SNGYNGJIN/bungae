function checkWebSocketStatus(socket) {
    if (!socket) {
        return "소켓 인스턴스가 존재하지 않습니다.";
    }

    switch (socket.readyState) {
        case WebSocket.CONNECTING:
            return "연결 중...";
        case WebSocket.OPEN:
            return "연결 됨";
        case WebSocket.CLOSING:
            return "연결 종료 중";
        case WebSocket.CLOSED:
            return "연결 종료됨";
        default:
            return "알 수 없는 상태";
    }
}