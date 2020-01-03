// @flow

import React from 'react'
import { ITEM_HEIGHT } from './ProcessChart'
import { Button} from '@blueprintjs/core'
import type { ProcessedTaskTemplateType } from './CreateProcessTemplate'
import styled from 'styled-components'

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
  taskTemplates: Array<ProcessedTaskTemplateType>,
  createTask: () => void,
  editTask: (id: number) => void
}

class TaskList extends React.Component<PropsType> {
  editTask = (id: number) => () => {
    this.props.editTask(id)
  }

  render () {
    return <ListContainer>
      {
        this.props.taskTemplates.map(
          node => <StyledButton className='bp3-minimal'
                          onClick={this.editTask(node.id)}
                          key={node.id}>{node.name}</StyledButton>
        )
      }
      <StyledButton style={{ marginTop: '10px' }}
              className='bp3-minimal' icon='add'
              text='Add task' onClick={this.props.createTask} />
    </ListContainer>
  }
}

export default TaskList
