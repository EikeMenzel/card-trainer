import { RxStompConfig } from '@stomp/rx-stomp';

export const myRxStompConfig: RxStompConfig = {
  brokerURL: 'ws://localhost:80/tmp/websocket',

  heartbeatIncoming: 1000,
  heartbeatOutgoing: 1000, //every 1 seconds

  reconnectDelay: 200, // 200 ms

  debug: (msg: string): void => {
    console.log(new Date(), msg);
  },
};
