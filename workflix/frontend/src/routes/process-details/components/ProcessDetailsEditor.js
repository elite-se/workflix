// @flow

import React from 'react'
import { FormGroup, InputGroup } from '@blueprintjs/core'
import styled from 'styled-components'
import type { UserType } from '../../../modules/datatypes/User'
import AutoSizeTextArea from '../../../modules/common/components/AutoSizeTextArea'
import handleStringChange from '../../../modules/common/handleStringChange'
import type { ProcessType } from '../../../modules/datatypes/Process'
import { DateInput } from '@blueprintjs/datetime'

const Third = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex-direction: column;
  width: 30%;
`

type PropsType = {
  onTitleChange: (title: string) => void,
  title: string,
  onDescriptionChange: (description: string) => void,
  description: string,
  onDeadlineChange: (deadline: Date) => void,
  deadline: Date,
  users: Map<string, UserType>,
  process: ProcessType
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
      deadline
    } = this.props

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
        </Third>
        <Third>
          <FormGroup label='Deadline' labelInfo='(required)'>
            <DateInput {...jsDateFormatter} value={deadline} onChange={onDeadlineChange}/>
          </FormGroup>
          <FormGroup label='Details'>{/* todo: Add some details of the process here. */}
          </FormGroup>
        </Third>
        <Third/>
      </div>
    </>
  }
}

export default ProcessDetailsEditor
