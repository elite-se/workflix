// @flow

import React from 'react'
import { FormGroup, InputGroup, NumericInput } from '@blueprintjs/core'
import AutoSizeTextArea from '../../common/components/AutoSizeTextArea'
import type { UserType } from '../../datatypes/User'
import styled from 'styled-components'
import UserSelect from '../../../modules/common/UserSelect'
import handleStringChange from '../../common/handleStringChange'

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
  onDurationLimitChange: (durationLimit: number) => void,
  durationLimit: ?number,
  users: Map<string, UserType>,
  owner: ?UserType,
  onOwnerChange: (owner: ?UserType) => void,
  highlightValidation: boolean
}

class ProcessTemplateDetailsEditor extends React.Component<PropsType> {
  render () {
    const {
      onTitleChange,
      title,
      onDescriptionChange,
      description,
      onDurationLimitChange,
      durationLimit,
      users,
      owner,
      onOwnerChange,
      highlightValidation
    } = this.props

    return <>
      <div style={{ display: 'flex' }}>
        <FormGroup label='Title' labelInfo='(required)'>
          <InputGroup large style={{ minWidth: '500px' }} onChange={handleStringChange(onTitleChange)} value={title}
                      placeholder='Add process template title...'
                      intent={highlightValidation && !title ? 'danger' : 'none'}/>
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
              placeholder={'Add description...\n\nWhat is this process about?\nWhen should it be initiated?'}/>
          </FormGroup>
        </Third>
        <Third>
          <FormGroup label='Duration Limit' labelInfo='(required, positive)'>
            <NumericInput onValueChange={onDurationLimitChange} min={0.1} stepSize={0.1}
                          intent={highlightValidation && (!durationLimit || durationLimit <= 0) ? 'danger' : 'none'}
                          value={durationLimit !== null ? durationLimit : ''} fill/>
          </FormGroup>
          <FormGroup label='Owner' labelInfo='(required)'>
            <UserSelect users={Array.from(users.values())} activeItem={owner} onItemSelect={onOwnerChange} fill
                        intent={highlightValidation && !owner ? 'danger' : 'none'}/>
          </FormGroup>
        </Third>
        <Third/>
      </div>
    </>
  }
}

export default ProcessTemplateDetailsEditor
