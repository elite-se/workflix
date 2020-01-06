// @flow

import React from 'react'
import { ITEM_HEIGHT } from './ProcessChart'
import { Button, Colors } from '@blueprintjs/core'
import styled from 'styled-components'
import type { IncompleteTaskTemplateType } from '../ProcessTemplateEditorTypes'

const StyledButton = styled(Button)`
  height: ${ITEM_HEIGHT}px;
  display: flex;
  flex: 1;
  justify-content: center;
`

const ListContainer = styled<{}, {}, 'div'>('div')`
  min-width: 100px;
  display: flex;
  flex-direction: column;
  align-items: stretch;
`

const ListItem = styled<{}, {}, 'div'>('div')`
  &:nth-child(2n + 1) {
    background-color: ${Colors.LIGHT_GRAY4};
  }
  display: flex;
  justify-content: stretch;
  padding-right: 20px;
`

type PropsType = {
  taskTemplates: IncompleteTaskTemplateType[],
  createTask: () => void,
  selectTaskId: (id: number) => void,
  selectedId: ?number,
  highlightAdd: boolean
}

class TaskList extends React.Component<PropsType> {
  selectTaskId = (id: number) => () => {
    this.props.selectTaskId(id)
  }

  render () {
    const { taskTemplates, selectedId, createTask, highlightAdd } = this.props
    return <ListContainer>
      {
        taskTemplates.map(
          node => <ListItem key={node.id}><StyledButton className='bp3-minimal'
                                          onClick={this.selectTaskId(node.id)}
                                          active={node.id === selectedId}>{node.name}</StyledButton></ListItem>
        )
      }
      <StyledButton style={{ marginTop: taskTemplates.length !== 0 ? '10px' : '0' }}
                    minimal icon='add'
                    intent={highlightAdd ? 'danger' : 'none'}
                    text='Add task' onClick={createTask}/>
    </ListContainer>
  }
}

export default TaskList
