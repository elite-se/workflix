// @flow

export type UserType = {|
  displayname: string,
  name: string,
  id: string,
  email: string
|}

export type UserRoleType = {|
  id: number,
  name: string,
  description: string,
  createdAt: string,
  memberIds: number[]
|}
