// @flow

import React from 'react'
import { ITEM_HEIGHT } from './ProcessChart'
import { Button } from '@blueprintjs/core'
import styled from 'styled-components'
import type { IncompleteTaskTemplateType } from './CreateProcessTemplate'

const StyledButton = styled(Button)`
  height: ${ITEM_HEIGHT}px;
  display: flex;
  align-items: center;
`

const ListContainer = styled<{}, {}, 'div'>('div')`
  margin: 0 20px;
  min-width: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
`

type PropsType = {
  taskTemplates: IncompleteTaskTemplateType[],
  createTask: () => void,
  selectTaskId: (id: number) => void,
  selectedId: ?number
}

class TaskList extends React.Component<PropsType> {
  selectTaskId = (id: number) => () => {
    this.props.selectTaskId(id)
  }

  render () {
    const { taskTemplates, selectedId, createTask } = this.props
    return <ListContainer>
      {
        taskTemplates.map(
          node => <StyledButton className='bp3-minimal'
                                onClick={this.selectTaskId(node.id)}
                                active={node.id === selectedId}
                                key={node.id}>{node.name}</StyledButton>
        )
      }
      <StyledButton style={{ marginTop: '10px' }}
                    className='bp3-minimal' icon='add'
                    text='Add task' onClick={createTask}/>
    </ListContainer>
  }
}

export default TaskList
