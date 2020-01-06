// @flow

import type { UserType } from '../datatypes/User'

export type IncompleteTaskTemplateType = {|
  id: number,
  name: string,
  description: string,
  estimatedDuration: ?number,
  necessaryClosings: number,
  responsibleUserRoleId: ?number,
  predecessors: number[]
|}

export type IncompleteProcessTemplateType = {|
  tasks: IncompleteTaskTemplateType[],
  title: string,
  description: string,
  durationLimit: ?number,
  owner: ?UserType
|}
