// @flow

export type UserType = {|
  displayname: string,
  name: string,
  id: string,
  email: string
|}

export type ProcessTemplateMasterDataType = {|
  id: number,
  title: string,
  durationLimit: ?number,
  owner: UserType
|}
