// @flow

export type ProcessGroupType = {
  id: number,
  title: string,
  description: string,
  ownerId: string,
  createdAt: Date,
  membersIds: string[],
  deleted?: boolean
}
