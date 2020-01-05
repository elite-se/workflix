// @flow

import type { ProcessStatusType } from './Process'
import type { UserType } from './User'
import type { ProcessGroupType } from './ProcessGroup'

export type FiltersType = {
  status?: ProcessStatusType[],
  involving?: ?UserType,
  processGroups?: ProcessGroupType[]
}
