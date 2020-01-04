// @flow

import type { ProcessStatusType } from './Process'
import type { UserType } from './User'

export type FiltersType = {
  status?: ProcessStatusType[],
  involving?: ?UserType
}
