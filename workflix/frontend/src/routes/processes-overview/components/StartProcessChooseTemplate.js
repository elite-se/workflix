// @flow

import React from 'react'
import withPromiseResolver from '../../../modules/app/hocs/withPromiseResolver'
import type { ProcessGroupType } from '../../../modules/datatypes/ProcessGroup'
import type { ProcessTemplateMasterDataType } from '../../../modules/datatypes/Process'
import ProcessApi from '../../../modules/api/ProcessApi'
import StartProcessForm from '../../process-templates/components/StartProcessForm'
import type { ItemPredicate } from '@blueprintjs/select'
import { ItemRenderer, Select } from '@blueprintjs/select'
import { Button, FormGroup, MenuItem } from '@blueprintjs/core'
import highlightText from '../../../modules/common/highlightText'

type PropsType = {|
  processGroups: Map<number, ProcessGroupType>,
  processTemplates: ProcessTemplateMasterDataType[]
|}

type StateType = {
  template: ?ProcessTemplateMasterDataType
}

const TemplateSelect = Select.ofType<ProcessTemplateMasterDataType>()

class StartProcessChooseTemplate extends React.Component<PropsType, StateType> {
  state = {
    template: null
  }

  itemRenderer: ItemRenderer<ProcessTemplateMasterDataType> = (item, { handleClick, modifiers, query }) => {
    return <MenuItem
      active={modifiers.active}
      disabled={modifiers.disabled}
      icon={this.state.template === item ? 'tick' : 'blank'}
      label={highlightText(item.title, query)}
      key={item.id}
      onClick={handleClick}
      shouldDismissPopover={false}
      text={highlightText(item.title, query)}/>
  }

  filterTemplates: ItemPredicate<ProcessTemplateMasterDataType> = (query, template, _index, exactMatch) => {
    const normalizedName = template.title.toLocaleLowerCase()
    const normalizedQuery = query.toLocaleLowerCase()
    return exactMatch
      ? [normalizedName].includes(normalizedQuery)
      : (normalizedName).indexOf(normalizedQuery) >= 0
  }

  onTemplateChanged = (template: ProcessTemplateMasterDataType) => this.setState({ template })

  render () {
    const { processTemplates, processGroups } = this.props
    const { template } = this.state
    return <div style={{
      width: '500px',
      padding: '20px',
      display: 'flex',
      flexDirection: 'column'
    }}>
      <FormGroup label='Process template' labelInfo='(required)'>
        <TemplateSelect popoverProps={{
          usePortal: false,
          fill: true
        }} items={processTemplates} itemPredicate={this.filterTemplates} onItemSelect={this.onTemplateChanged}
                        itemRenderer={this.itemRenderer}>
          <Button icon='gantt-chart' fill>{template?.title || 'Select process template...'}</Button>
        </TemplateSelect>
      </FormGroup>
      {template && <StartProcessForm noPadding template={template} processGroups={processGroups}/>}
    </div>
  }
}

const promiseCreator = () => new ProcessApi().getAllProcessTemplates()
  .then(templates => ({
    processTemplates: templates.filter(template => !template.deleted)
  }))

export default withPromiseResolver<PropsType, *>(promiseCreator)(StartProcessChooseTemplate)
