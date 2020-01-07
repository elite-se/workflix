// @flow

export type UserType = {|
  id: string,
  name: string,
  displayname: string,
  email: string,
  userRoleIds: number[],
  processGroupIds: number[]
|}

export type UserRoleType = {|
  id: number,
  name: string,
  description: string,
  createdAt: Date,
  memberIds: number[]
|}
