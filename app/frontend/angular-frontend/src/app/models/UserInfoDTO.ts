export interface UserInfoDTO {
  username: string,
  email: string,
  cardsToLearn: number,
  receiveLearnNotification: boolean,
  langCode: string,
  achievementIds: [number]
}
