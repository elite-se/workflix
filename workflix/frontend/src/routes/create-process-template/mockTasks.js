// @flow

import type { NodeType } from './CreateProcessTemplate'

/*
graph:  1 ----- 3 - 6
              /       \
        0 - 2 - 4 - 5 - 7
 */

const MOCK_TASKS: Array<NodeType> = [
  {
    id: 0,
    predecessors: [],
    duration: 1,
    title: 'Do stuff 0'
  },
  {
    id: 1,
    predecessors: [],
    duration: 2,
    title: 'Do stuff 1'
  },
  {
    id: 2,
    predecessors: [0],
    duration: 1.4,
    title: 'Do stuff 2'
  },
  {
    id: 3,
    predecessors: [1, 2],
    duration: 1.3,
    title: 'Do stuff 3'
  },
  {
    id: 4,
    predecessors: [2],
    duration: 2,
    title: 'Do stuff 4'
  },
  {
    id: 5,
    predecessors: [4],
    duration: 2,
    title: 'Do stuff 5'
  },
  {
    id: 6,
    predecessors: [3],
    duration: 1,
    title: 'Do stuff 6'
  },
  {
    id: 7,
    predecessors: [5, 6],
    duration: 1,
    title: 'Do stuff 7'
  }
]


export default MOCK_TASKS
