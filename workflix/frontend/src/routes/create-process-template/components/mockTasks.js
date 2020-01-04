// @flow

/* eslint-disable no-magic-numbers */

/*
graph:  1 ----- 3 - 6
              /       \
        0 - 2 - 4 - 5 - 7
*/

import type { IncompleteTaskTemplateType } from './CreateProcessTemplate'

const MOCK_TASK_TEMPLATES: Array<IncompleteTaskTemplateType> = [
  {
    id: 0,
    predecessors: [],
    estimatedDuration: 1,
    name: 'Do stuff 0',
    description: '',
    necessaryClosings: 1,
    responsibleUserRoleId: null
  },
  {
    id: 1,
    predecessors: [],
    estimatedDuration: 2,
    name: 'Do stuff 1',
    description: '',
    necessaryClosings: 1,
    responsibleUserRoleId: null
  },
  {
    id: 2,
    predecessors: [0],
    estimatedDuration: 1.4,
    name: 'Do stuff 2',
    description: '',
    necessaryClosings: 1,
    responsibleUserRoleId: null
  },
  {
    id: 3,
    predecessors: [1, 2],
    estimatedDuration: 1.3,
    name: 'Do stuff 3',
    description: '',
    necessaryClosings: 1,
    responsibleUserRoleId: null
  },
  {
    id: 4,
    predecessors: [2],
    estimatedDuration: 2,
    name: 'Do stuff 4',
    description: '',
    necessaryClosings: 1,
    responsibleUserRoleId: null
  },
  {
    id: 5,
    predecessors: [4],
    estimatedDuration: 2,
    name: 'Do stuff 5',
    description: '',
    necessaryClosings: 1,
    responsibleUserRoleId: null
  },
  {
    id: 6,
    predecessors: [3],
    estimatedDuration: 1,
    name: 'Do stuff 6',
    description: '',
    necessaryClosings: 1,
    responsibleUserRoleId: null
  },
  {
    id: 7,
    predecessors: [5, 6],
    estimatedDuration: 1,
    name: 'Do stuff 7',
    description: '',
    necessaryClosings: 1,
    responsibleUserRoleId: null
  }
]

export default MOCK_TASK_TEMPLATES
