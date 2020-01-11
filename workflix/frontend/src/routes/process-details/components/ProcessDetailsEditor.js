// @flow

import React from 'react'
import { FormGroup, Icon, InputGroup } from '@blueprintjs/core'
import type { StyledComponent } from 'styled-components'
import styled from 'styled-components'
import type { UserType } from '../../../modules/datatypes/User'
import AutoSizeTextArea from '../../../modules/common/components/AutoSizeTextArea'
import handleStringChange from '../../../modules/common/handleStringChange'
import type { ProcessTemplateType, ProcessType } from '../../../modules/datatypes/Process'
import { DateInput } from '@blueprintjs/datetime'
import type { ProcessGroupType } from '../../../modules/datatypes/ProcessGroup'
import ProcessProgress from '../../../modules/common/components/ProcessProgress'

const Third = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex-direction: column;
  width: 30%;
`

const Item: StyledComponent<{}, {}, *> = styled('div')`
  display: flex;
  flex-direction: row;
  align-items: center;
  & > * {
    padding: 5px;
  }
`

type PropsType = {
  onTitleChange: (title: string) => void,
  title: string,
  onDescriptionChange: (description: string) => void,
  description: string,
  onDeadlineChange: (deadline: Date) => void,
  deadline: Date,
  users: Map<string, UserType>,
  process: ProcessType,
  processGroups: Map<number, ProcessGroupType>,
  template: ProcessTemplateType
}

const jsDateFormatter = {
  formatDate: date => date.toLocaleDateString(),
  parseDate: str => new Date(str),
  placeholder: 'M/D/YYYY'
}

class ProcessDetailsEditor extends React.Component<PropsType> {
  render () {
    const {
      onTitleChange,
      title,
      onDescriptionChange,
      description,
      onDeadlineChange,
      deadline,
      process,
      users,
      processGroups,
      template
    } = this.props

    const starter = users.get(process.starterId)
    const processGroup = processGroups.get(process.processGroupId)

    return <>
      <div style={{ display: 'flex' }}>
        <FormGroup label='Title' labelInfo='(required)'>
          <InputGroup large style={{ minWidth: '500px' }} onChange={handleStringChange(onTitleChange)} value={title}
                      placeholder='Add process title...'
                      intent={!title ? 'danger' : 'none'}/>
        </FormGroup>
      </div>
      <div style={{
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'stretch'
      }}>
        <Third>
          <FormGroup label='Description'>
            <AutoSizeTextArea
              style={{ resize: 'none' }} minRows={4} className='bp3-fill'
              onChange={handleStringChange(onDescriptionChange)} value={description}
              placeholder={'Add description...\n\nWhat is this process about?'}/>
          </FormGroup>
          <FormGroup label='Deadline' labelInfo='(required)'>
            <DateInput {...jsDateFormatter} value={deadline} fill onChange={onDeadlineChange}/>
          </FormGroup>
        </Third>
        <Third>
          <FormGroup label='Details'>
            <Item><Icon icon='time'/><span>{`Started at ${process.startedAt.toLocaleString()}`}</span></Item>
            {starter && <Item>
              <Icon icon='user'/><span>{`Started by ${starter.name} (${starter.displayname})`}</span>
            </Item>}
            {processGroup && <Item>
              <Icon icon='office'/><span>{`Process group: ${processGroup.title}`}</span>
            </Item>}
            {template && <Item>
              <Icon icon='gantt-chart'/><span>{`From process template: ${template.title}`}</span>
            </Item>}
          </FormGroup>
          <FormGroup label='Process'>
            <ProcessProgress process={process}/>
          </FormGroup>
        </Third>
        <Third/>
      </div>
    </>
  }
}

export default ProcessDetailsEditor
