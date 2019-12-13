// @flow

import type { TaskType } from './TaskType'
import type { ProcessMasterDataType } from './ProcessMasterDataType'

export type ProcessType = {
  masterData: ProcessMasterDataType,
  tasks: Array<TaskType>
}
